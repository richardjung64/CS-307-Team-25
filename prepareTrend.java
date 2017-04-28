private class prepareTrend extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void...Params) {
            try{
                DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                DynamoDBScanExpression scanR = new DynamoDBScanExpression();
                List<ClothingDO> scanList = mapper.scan(ClothingDO.class, scanR);
                if(scanList == null) {
                    Log.d("AsyncTrend", "scanList == null");
                }
                Collections.sort(scanList, new likesComparator());
                List<ClothingDO> rank = new ArrayList();
                int count = 0;
                for(ClothingDO cloth : scanList){
                    count++;
                    if(count <= 5){
                        rank.add(cloth);
                    }
                    else
                        break;
                }
                if(rank.isEmpty()){
                    Log.d("Inside AsyncSort", "rankList is empty");
                }
                itemRank = rank;
            } catch(Exception ex) {
                Log.d("AsyncScanClothing", "catch an exception");
            };

            return null;
        }
    }