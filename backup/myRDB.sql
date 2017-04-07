

create table Users (
	userID int NOT NULL,
	username varchar(20),
	FollowerListNum int,
	FollowingListNum int,
	WishListNum int,
	WardrobeNum int,
	POSTListNum int,
	age int,
	gender char(2),
	isPrivate char(2),
	email varchar(30),
	uPic varchar(80), 
	bio varchar(100)
)

create table Companies (
	compID int NOT NULL;
	compname varchar(20),
	bio varchar(100),
	ProductsListNum int,
	FollowerListNum int,
	FollowingListNum int,
	logo varchar(100),
	weblink varchar(100),
)

create table Wardrobes (
	WardrobeID int NOT NULL,
	

)