//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.16
//
package com.amazonaws.mobile.content;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobile.mobilehelper.auth.IdentityManager;
import com.amazonaws.mobile.util.StringFormatUtils;
import com.amazonaws.mobile.mobilehelper.util.ThreadUtils;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Content Manager manages caching and transfer of files from Amazon S3 and/or
 * Amazon CloudFront. It lists files directly using S3, regardless of whether Amazon
 * CloudFront is in use. It maintains a size-limited cache for files stored on the
 * local device and provides operations to set the cache size limit and clear files
 * from the local cache. It serves as the application's interface into the Content
 * Delivery feature. The application can create and use any number of Content Managers
 * simultaneously, each with its own Amazon S3 bucket, S3 folder prefix, and local
 * on-device cache folder location.
 */
public class ContentManager implements Iterable<ContentItem> {

    /**
     * Logging tag for this class.
     */
    private static final String LOG_TAG = ContentManager.class.getSimpleName();

    /**
     * The path suffix for storing local content.
     */
    private static final String LOCAL_CONTENT_DIR_SUFFIX = "/content";

    /**
     * The path suffix for storing incoming content.
     */
    private static final String LOCAL_CONTENT_XFER_DIR_SUFFIX = "/incoming";

    private static final String DIR_DELIMITER = "/" ;

    /**
     * Amazon S3 Client to use for obtaining content.
     */
    protected final AmazonS3Client s3Client;

    /**
     * The transfer manager to manage transfers.
     */
    protected final TransferHelper transferHelper;

    /**
     * The S3 bucket to use for transfers.
     */
    protected final String bucket;

    /**
     * s3 Objects managed by this content manager use this object prefix.
     */
    protected final String s3DirPrefix;

    /**
     * The application context.
     */
    Context context;

    /**
     * The local content cache.
     */
    private final LocalContentCache localContentCache;

    /**
     * The local path to downloaded content.
     */
    protected final String localContentPath;

    /**
     * The local path to content being downloaded.
     */
    protected final String localTransferPath;


    /** The thread that handles iterating through the content and adding it to the queue. */
    protected final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public interface BuilderResultHandler {
        void onComplete(ContentManager contentManager);
    }

    /** Builder for convenience of instantiation.  */
    public static final class Builder {
        private Context context = null;
        private IdentityManager identityManager = null;
        private String bucket = null;
        private String s3DirPrefix = null;
        private String cloudFrontDomainName = null;
        private String basePath = null;
        private ClientConfiguration clientConfiguration;
        private Regions region = null;

        public Builder withContext(final Context context) {
            this.context = context;
            return this;
        }
        public Builder withIdentityManager(final IdentityManager identityManager) {
            this.identityManager = identityManager;
            return this;
        }
        public Builder withS3Bucket(final String s3Bucket) {
            this.bucket = s3Bucket;
            return this;
        }

        public Builder withS3DirPrefix(final String s3DirPrefix) {
            this.s3DirPrefix = s3DirPrefix;
            return this;
        }

        public Builder withCloudFrontDomainName(final String cloudFrontDomainName) {
            this.cloudFrontDomainName = cloudFrontDomainName;
            return this;
        }


        public Builder withLocalBasePath(final String basePath) {
            this.basePath = basePath;
            return this;
        }

        public Builder withRegion(final Regions region){
            this.region = region;
            return this;
        }

        public Builder withClientConfiguration(final ClientConfiguration clientConfiguration) {
            this.clientConfiguration = clientConfiguration;
            return this;
        }


        public void build(final BuilderResultHandler resultHandler) {
            if (clientConfiguration == null) {
                clientConfiguration = new ClientConfiguration();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ContentManager contentManager =
                        new ContentManager(context, identityManager, bucket, s3DirPrefix,
                            cloudFrontDomainName, basePath, region, clientConfiguration);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultHandler.onComplete(contentManager);
                        }
                    });
                }
            }).start();
        }
    }

    /**
     * Constructs a content manager.
     *
     * @param context an Android context.
     * @param identityManager identity manager to use for credentials.
     * @param bucket the s3 bucket.
     * @param s3DirPrefix The directory within the bucket for which this content manager will manage content.
     *                    This may be passed as null if the root directory of the bucket should be used. The
     *                    object delimiter is always the standard directory separator of '/'.
     * @param cloudFrontDomainName The CloudFront domain name where this bucket's content may be
     *                             retrieved by downloading over http from a CloudFront edge
     *                             location.
     * @param basePath the base path under which to store the files managed by this content
     *                        manager.  This path will have a subdirectory identifying the remote
     *                        location, and beneath that subdirectories 'content' and 'incoming'
     *                        will be created to store the locally cached content and incoming
     *                        transfers respectively.
     * @param region The aws region.
     * @param clientConfiguration The client configuration for AWS clients.
     */
    ContentManager(final Context context,
		   final IdentityManager identityManager,
		   final String bucket,
		   final String s3DirPrefix,
		   final String cloudFrontDomainName,
		   final String basePath,
		   final Regions region,
		   final ClientConfiguration clientConfiguration) {

        this.context = context.getApplicationContext();

        s3Client = new AmazonS3Client(identityManager.getCredentialsProvider(), clientConfiguration);
        s3Client.setRegion(Region.getRegion(region));

        this.bucket = bucket;

        final String localDirPrefix;

        if (s3DirPrefix != null && !s3DirPrefix.isEmpty()) {
            if (s3DirPrefix.endsWith(DIR_DELIMITER)) {
                localDirPrefix = "/" + s3DirPrefix.substring(0, s3DirPrefix.length() - 1);
                this.s3DirPrefix = s3DirPrefix;
            } else {
                localDirPrefix = "/" + s3DirPrefix;
                this.s3DirPrefix = s3DirPrefix + DIR_DELIMITER;
            }
        } else {
            localDirPrefix = "";
            this.s3DirPrefix = null;
        }

        final String baseContentPath = basePath + "/s3_" + bucket + localDirPrefix;

        final File prefixPathFile = new File(baseContentPath);

        if (!prefixPathFile.exists()) {
            if (!prefixPathFile.mkdirs()) {
                throw new RuntimeException(String.format(
                    "Can't create directory the base directory ('%s') for storing local content.",
                    baseContentPath));
            }
        }

        if (!prefixPathFile.isDirectory()) {
            throw new RuntimeException(
                String.format("Prefix content path '%s' is not a directory.", baseContentPath));
        }

        localContentPath = baseContentPath  + LOCAL_CONTENT_DIR_SUFFIX;
        localTransferPath = baseContentPath + LOCAL_CONTENT_XFER_DIR_SUFFIX;

        localContentCache = new LocalContentCache(context, "com.amazonaws.mobile.content.cache.s3."
            + bucket + localDirPrefix.replace("/", "."), localContentPath);

        if (cloudFrontDomainName == null) {
            transferHelper = S3TransferHelper.build(context, s3Client, bucket,
                    this.s3DirPrefix, localTransferPath, localContentCache);
        } else {
            transferHelper =
                new CloudFrontTransferHelper(context, cloudFrontDomainName,
                    this.s3DirPrefix, localTransferPath, localContentCache);
        }
    }

    /**
     * @return the maximum number of bytes this cache may hold.
     */
    public long getContentCacheSize() {
        return localContentCache.getMaxCacheSize();
    }

    /**
     * @return the number of bytes used by the cache.
     */
    public long getCacheUsedSize() {
        return localContentCache.getCacheSizeUsed();
    }

    /**
     * Number of bytes pinned to the cache.
     */
    public long getPinnedSize() {
        return localContentCache.getBytesPinned();
    }

    /**
     * Set the cache size to be used by this content manager.  This immediately removes the oldest
     * content by last modified time until the new cache size is not exceeded.
     *
     * @param maxCacheSize the new max cache size.
     */
    public void setContentCacheSize(final long maxCacheSize) {
        localContentCache.setMaxCacheSize(maxCacheSize);
    }

    /**
     * Remove local content from the cache.
     * @param filePath the path to the content to remove.
     * @return true if file will be removed asyncronously, false if the file could not be removed.
     */
    public boolean removeLocal(final String filePath) {
        return localContentCache.removeFile(filePath);
    }

    /**
     * Clear the local content cache.
     */
    public void clearCache() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                localContentCache.clear();
            }
        });
    }

    /**
     * @return The path to local content managed by this content manager.
     */
    public String getLocalContentPath() {
        return localContentPath;
    }

    /* package */ ContentState getContentStateForTransfer(final String filePath) {
        if (!transferHelper.isTransferring(filePath)) {
            return ContentState.REMOTE;
        }

        boolean localContentAvailable = localContentCache.contains(filePath);

        if (transferHelper.isTransferWaiting(filePath)) {
            if (localContentAvailable) {
                return ContentState.CACHED_NEW_VERSION_TRANSFER_WAITING;
            }
            return ContentState.TRANSFER_WAITING;
        }
        if (localContentAvailable) {
            return ContentState.CACHED_TRANSFERRING_NEW_VERSION;
        }
        return ContentState.TRANSFERRING;
    }

    /* package */ LocalContentCache getLocalContentCache() {
        return localContentCache;
    }

    /* package */ String getS3bucket() {
        return bucket;
    }

    /* package */ AmazonS3Client getS3Client() {
        return s3Client;
    }

    /**
     * Clears all progress listeners associated with this content manager.
     */
    public void clearProgressListeners() {
        transferHelper.clearProgressListeners();
    }

    /**
     * Clears all listeners associated with this content manager.
     */
    public void clearAllListeners() {
        clearProgressListeners();
        localContentCache.setContentRemovedListener(null);
    }

    /**
     * Set a listener for any content removed from the cache managed by this content manager.
     * @param listener the listening handler.
     */
    public void setContentRemovedListener(final ContentRemovedListener listener) {
        localContentCache.setContentRemovedListener(listener);
    }

    /**
     * Set the progress listener for a file managed by the content manager.
     * @param filePath the relative path and file name.
     * @param listener the listening handler.
     */
    public void setProgressListener(final String filePath, final ContentProgressListener listener) {
        transferHelper.setProgressListener(filePath, listener);
    }

    /**
     * Adds a flag that instructs the content manager to keep the file and not count it toward the
     * cache size.
     *
     * @param filePath the relative path and file name.
     */
    public void pinContent(final String filePath) {
        getContent(filePath, 0, ContentDownloadPolicy.DOWNLOAD_IF_NOT_CACHED, true, null);
    }

    /**
     * Adds a flag that instructs the content manager to keep the file and not count it toward the
     * cache size. Optionally takes a listener to receive updates if a download is started as a
     * result of attempting to pin content that is not yet cached.
     *
     * @param filePath the relative path and file name.
     */
    public void pinContent(final String filePath, final ContentProgressListener listener) {
        getContent(filePath, 0, ContentDownloadPolicy.DOWNLOAD_IF_NOT_CACHED, true, listener);
    }

    /**
     * Removes the flag that instructs the content manager to keep the file and not count it toward
     * the cache size.
     * @param filePath the relative path and file name.
     * @param afterUnpinRunner runnable to be run after the content is unpinned from the cache.
     */
    public void unPinContent(final String filePath, final Runnable afterUnpinRunner) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                localContentCache.unPinFile(filePath);
                if (afterUnpinRunner != null) {
                    ThreadUtils.runOnUiThread(afterUnpinRunner);
                }
            }
        });
    }

    /**
     * Checks whether a file has been pinned.
     *
     * @param filePath the relative path and file name.
     * @return true if content has been marked to be kept by calling {@link #pinContent(String)},
     *         otherwise returns false.
     */
    public boolean isContentPinned(final String filePath) {
        return localContentCache.shouldPinFile(filePath);
    }

    /**
     * Get content by file name. Downloads and caches the remote content and calls the listener's
     * onSuccess method once the content is ready to be accessed.
     *
     * @param filePath the relative path and file name of the content to retrieve.
     * @param listener listener to receive the result.
     */
    public void getContent(final String filePath, final ContentProgressListener listener) {
        getContent(filePath, 0, ContentDownloadPolicy.DOWNLOAD_IF_NOT_CACHED, false, listener);
    }

    /**
     * Get content by file name. Downloads and caches the remote content if it is not available or
     * if the alwaysDownload flag is set.  Calls the listener's onSuccess method once the content
     * is ready to be accessed.
     *
     * @param filePath the relative path and file name of the content to retrieve.
     * @param optionalFileSize an optional file size to be checked against the space available in
     *                         the cache.
     * @param policy indicates the download policy.  See {@link ContentDownloadPolicy}
     * @param listener listener to receive the result.
     */
    public void getContent(final String filePath,
                           final long optionalFileSize,
                           final ContentDownloadPolicy policy,
                           final boolean pinOnCompletion,
                           final ContentProgressListener listener) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                File localFile;
                if (pinOnCompletion) {
                    localContentCache.pinFile(filePath);
                }
                if (policy == ContentDownloadPolicy.DOWNLOAD_ALWAYS) {
                    // ignore that the file may be in cache when the policy is to always download.
                    localFile = null;
                } else {
                    localFile = localContentCache.get(filePath);

                    if (policy == ContentDownloadPolicy.DOWNLOAD_METADATA_IF_NOT_CACHED && localFile == null) {
                        try {
                            final String s3Key = s3DirPrefix != null ? s3DirPrefix + filePath : filePath;
                            final ObjectMetadata objectMeta =
                                s3Client.getObjectMetadata(bucket, s3Key);

                            // Check if the object is transferring and adjust the state appropriately.
                            final ContentState contentState;
                            if (transferHelper.isTransferring(filePath)) {
                                if (transferHelper.isTransferWaiting(filePath)) {
                                    contentState = ContentState.TRANSFER_WAITING;
                                } else {
                                    contentState = ContentState.TRANSFERRING;
                                }
                            } else {
                                contentState = ContentState.REMOTE;
                            }

                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(
                                        new S3ContentMeta(filePath, objectMeta, contentState));
                                }
                            });
                        } catch (final AmazonClientException ex) {
                            Log.d(LOG_TAG, ex.getMessage(), ex);
                            if (listener != null) {
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onError(filePath, ex);
                                    }
                                });
                            }
                        }
                        return;
                    }
                }

                final boolean isPolicyDownloadIfNewer =
                    policy == ContentDownloadPolicy.DOWNLOAD_IF_NEWER_EXIST;

                final long fileSize;

                if (localFile != null && (isPolicyDownloadIfNewer || optionalFileSize == 0)) {
                    try {
                        final String s3Key = s3DirPrefix != null ? s3DirPrefix + filePath : filePath;
                        final ObjectMetadata objectMeta =
                            s3Client.getObjectMetadata(bucket,  s3Key);
                        // Set the file size from the retrieved meta data.
                        fileSize = objectMeta.getContentLength();

                        // If the remote file is newer.
                        if (isPolicyDownloadIfNewer &&
                            localFile.lastModified() < objectMeta.getLastModified().getTime()) {
                            // Ignore the local file and force a download.
                            localFile = null;
                        }
                    } catch (final AmazonClientException ex) {
                        Log.d(LOG_TAG, ex.getMessage(), ex);
                        if (listener != null) {
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onError(filePath, ex);
                                }
                            });
                        }
                        return;
                    }
                } else {
                    fileSize = optionalFileSize;
                }

                if (localFile != null) {
                    final File result = localFile;
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(new FileContent(result,
                                localContentCache.absolutePathToRelativePath(result.getAbsolutePath())));
                        }
                    });
                    return;
                }

                // Don't attempt to download if the file doesn't exist and the policy is never to
                // download.
                if (policy == ContentDownloadPolicy.DOWNLOAD_NEVER) {
                    Log.d(LOG_TAG, String.format(
                        "Policy set to never DOWNLOAD_NEVER and the file(%s) was not cached.",
                        filePath), new FileNotFoundException());
                    if (listener != null) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(filePath, new FileNotFoundException(
                                    "Policy set to DOWNLOAD_NEVER and the file was not cached."));
                            }
                        });
                    }
                    return;
                }

                if (!pinOnCompletion && !localContentCache.shouldPinFile(filePath)) {
                    final long sizeTransferring = transferHelper.getSizeTransferring();
                    // Check if there is space if the file size is available
                    final Exception ex = getExceptionIfNoSpace(filePath, fileSize, sizeTransferring);
                    if (ex != null) {
                        Log.d(LOG_TAG, ex.getMessage(), ex);
                        if (listener != null) {
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onError(filePath, ex);
                                }
                            });
                        }
                        return;
                    }
                }
                // Begin downloading content.
                transferHelper.download(filePath, fileSize, listener);
            }
        });
    }

    private Exception getExceptionIfNoSpace(final String filePath, final long fileSize, final long sizeTransferring) {
        // if this file can't fit in our cache.
        if (fileSize > localContentCache.getMaxCacheSize()) {
            return new IllegalStateException(
                String.format("Adding '%s' of size %s would exceed the cache size by %s bytes.",
                    filePath, StringFormatUtils.getBytesString(fileSize, true),
                    StringFormatUtils.getBytesString(fileSize - localContentCache.getMaxCacheSize(), true)));
        }
        final long bytesOverSize = (sizeTransferring + fileSize) -
            localContentCache.getMaxCacheSize();

        if (bytesOverSize > 0) {
            Log.w(LOG_TAG, String.format("Adding '%s' of size %s causes in progress transfers to" +
                " exceed the cache size by %s bytes. Content that completes downloading first" +
                " will be dropped.", filePath, StringFormatUtils.getBytesString(fileSize, true),
                StringFormatUtils.getBytesString(bytesOverSize, true)));
        }
        return null;
    }

    /**
     * Compares two content items by date first and then by name.
     */
    private static Comparator<ContentItem> compareContentItemsByDateAndName
        = new Comparator<ContentItem>() {
        @Override
        public int compare(ContentItem lhs, ContentItem rhs) {
            long rhTime =  rhs.getLastModifiedTime();
            long lhTime = lhs.getLastModifiedTime();
            if (rhTime != lhTime) {
                return rhTime > lhTime ? 1 : -1;
            }
            return lhs.getFilePath().compareTo(rhs.getFilePath());
        }
    };

    /**
     * Downloads recent content managed by the content manager. Stops upon reaching the first item
     * that is too large to fit in the remaining available cache space.
     */
    class DownloadRecentS3ContentRunner implements Runnable {
        final String s3Prefix;
        final String localPathPrefix;
        final ContentProgressListener listener;
        DownloadRecentS3ContentRunner(final String filePathPrefix, final ContentProgressListener listener) {
            this.localPathPrefix = filePathPrefix;
            this.s3Prefix = getS3PathPrefix(filePathPrefix);
            this.listener = listener;
        }

        @Override
        public void run() {
            final AvailableS3ContentIterator contentIterator = new AvailableS3ContentIterator(
                ContentManager.this, s3Prefix, localPathPrefix, null, executorService, false);
            final TreeSet<ContentItem> sortedItems =
                new TreeSet<>(compareContentItemsByDateAndName);

            try {
                // Retrieve all content items and sort by most recent using TreeSet.
                for (final ContentItem contentItem : contentIterator) {
                    sortedItems.add(contentItem);
                }
            } catch (final Exception ex) {
                Log.e(LOG_TAG, ex.getMessage(), ex);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(null, ex);
                    }
                });
                return;
            }

            long availableSpaceInCache = localContentCache.getMaxCacheSize();

            // Load the cache with all items that can fit.
            for (final ContentItem contentItem : sortedItems) {
                if (contentItem.getSize() > availableSpaceInCache) {
                    break;
                }
                final String filePath = contentItem.getFilePath();
                if (!localContentCache.shouldPinFile(filePath)) {
                    availableSpaceInCache -= contentItem.getSize();
                }
                if (contentItem.getContentState() ==
                    ContentState.CACHED_WITH_NEWER_VERSION_AVAILABLE ||
                    contentItem.getContentState() == ContentState.REMOTE) {
                    Log.d(LOG_TAG, "Downloading recent content for file: " + contentItem.getFilePath());
                    getContent(contentItem.getFilePath(), contentItem.getSize(),
                        ContentDownloadPolicy.DOWNLOAD_ALWAYS, false, listener);
                }
            }
        }
    }

    /**
     * Preload most recent content until reaching the first content item that is too large to fit
     * in the remaining available cache space.
     *
     * @param listener an optional progress listener to use for all the downloads that are created.
     */
    public void downloadRecentContent(final ContentProgressListener listener) {
        downloadRecentContent(null, listener);
    }

    /**
     * @return prefix of the folder in the S3 bucket.
     */
    public String getS3DirPrefix() {
        return s3DirPrefix;
    }

    /**
     * Retrieves the full S3 object prefix given a file name prefix.
     *
     * The S3 path to the files beginning with a prefix needs to take into consideration the
     * directory within the bucket that this content manager manages, as well as the file name
     * prefix if one exists.
     *
     * @param filePathPrefix the file name prefix for a file in s3.
     * @return the full S3 object name prefix.
     */
    protected String getS3PathPrefix(String filePathPrefix) {
        if (filePathPrefix != null && !filePathPrefix.isEmpty()) {
            if (s3DirPrefix != null) {
                return s3DirPrefix + filePathPrefix;
            }
            return filePathPrefix;
        }
        return s3DirPrefix;
    }

    /**
     * Preload most recent content with names beginning with a prefix until the cache is full.
     *
     * @param filePathPrefix File names must begin with this prefix to be considered for download.
     * @param listener an optional progress listener to use for all the downloads that are created. Note that
     *                 if there is an error listing, the {@link ContentProgressListener#onError(String, Exception)}
     *                 method will be invoked with a null filePath.
     */
    public void downloadRecentContent(final String filePathPrefix,
                                      final ContentProgressListener listener) {


        executorService.execute(new DownloadRecentS3ContentRunner(filePathPrefix, listener));
    }

    /**
     * Creates an iterator over all available content managed by this content manager.
     *
     * @return the iterator.
     */
    @Override
    public AvailableS3ContentIterator iterator() {
        return new AvailableS3ContentIterator(this, s3DirPrefix, "",
            DIR_DELIMITER, executorService, true);
    }

    /**
     * Get an iterator over all files beginning with a specified prefix.
     * @param filePathPrefix the prefix for all file names that will be included by the iterator.
     * @return the iterator.
     */
    public AvailableS3ContentIterator getAvailableContentIterator(final String filePathPrefix) {
        return new AvailableS3ContentIterator(this, getS3PathPrefix(filePathPrefix), filePathPrefix,
            DIR_DELIMITER, executorService, true);
    }

    /**
     * Get an iterator over all files beginning with a specified prefix.
     * @param filePathPrefix the prefix for all file names that will be included by the iterator.
     * @return the iterator.
     */
    public AvailableS3ContentIterator getAvailableContentIterator(final String filePathPrefix,
                                                                  final boolean includeDirectories) {
        return new AvailableS3ContentIterator(this, getS3PathPrefix(filePathPrefix), filePathPrefix,
            DIR_DELIMITER, executorService, includeDirectories);
    }

    private class ContentLister implements Runnable {
        final ContentListHandler listHandler;
        final String prefix;
        int startIndex;
        AvailableS3ContentIterator availableS3ContentIterator;

        ContentLister(final String prefix, final ContentListHandler listHandler) {
            this.listHandler = listHandler;
            this.prefix = prefix;
            startIndex = 0;
        }

        private void addContentItems(final int startIndex,
                                     final List<ContentItem> contentItems,
                                     final boolean hasMoreResults) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!listHandler.onContentReceived(startIndex, contentItems, hasMoreResults)) {
                        // if the user has requested to cancel listing content.
                        availableS3ContentIterator.cancel();
                    }
                }
            });
        }

        @Override
        public void run() {
            availableS3ContentIterator = getAvailableContentIterator(prefix);

            try {
                ArrayList<ContentItem> contentItems = new ArrayList<>();
                for (final ContentItem contentItem : availableS3ContentIterator) {
                    Log.d(LOG_TAG, "Found file: " + contentItem.getFilePath());

                    contentItems.add(contentItem);
                    // When we determine getting more content will block
                    if (availableS3ContentIterator.willNextBlock()) {
                        final int itemCount = contentItems.size();
                        // Add items so far to the UI.
                        addContentItems(startIndex, contentItems, true);
                        // Start a new list of items to add.
                        contentItems = new ArrayList<>();
                        startIndex += itemCount;
                    }
                }

                addContentItems(startIndex, contentItems, false);
            } catch (final Exception ex) {
                Log.e(LOG_TAG, ex.getMessage(), ex);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listHandler.onError(ex);
                    }
                });
            }
        }
    };

    /**
     * List all available content on the UI thread in batches of results.
     * @param handler the handler for receiving results.
     */
    public void listAvailableContent(final ContentListHandler handler) {
        executorService.execute(new ContentLister("", handler));
    }

    /**
     * List all available content whose file names begin with a specified prefix. Content
     * comes back through a handler on the UI thread in batches of results.  The handler
     * {@link ContentListHandler#onContentReceived(int, List, boolean)}  allows for
     * requesting to canceling listing; however since the callbacks are on the UI thread,
     * the handler may be called several more times for any queued results that have
     * already been received.
     * @param filePathPrefix the path and file name prefix for listing.
     * @param handler the handler for receiving results.
     */
    public void listAvailableContent(final String filePathPrefix,
                                      final ContentListHandler handler) {
        executorService.execute(new ContentLister(filePathPrefix, handler));
    }
	
    /** This must should be called once the content manager is no longer needed.  No methods should be called
     * on the ContentManager once this method has been called.
     */
    public synchronized void destroy() {
        transferHelper.destroy();
    }
}
