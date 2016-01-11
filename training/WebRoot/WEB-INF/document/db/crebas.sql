/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2013/2/27 15:14:07                           */
/*==============================================================*/


drop table if exists T_Bill;

drop table if exists T_BillDetail;

drop table if exists T_Class;

drop table if exists T_Classroom;

drop table if exists T_Commodity;

drop table if exists T_CommodityType;

drop table if exists T_Course;

drop table if exists T_CourseType;

drop table if exists T_FeeItem;

drop table if exists T_Holiday;

drop table if exists T_LessonDegree;

drop table if exists T_Media;

drop table if exists T_Payment;

drop table if exists T_PeriodTime;

drop table if exists T_PotCourse;

drop table if exists T_Potential;

drop table if exists T_PotentialStuStatus;

drop table if exists T_RecRej;

drop table if exists T_RecRejDetail;

drop table if exists T_Reply;

drop table if exists T_Right;

drop table if exists T_Role;

drop table if exists T_RoleRight;

drop table if exists T_School;

drop table if exists T_Store;

drop table if exists T_StuClass;

drop table if exists T_Student;

drop table if exists T_Supplier;

drop table if exists T_SystemConfig;

drop table if exists T_Unit;

drop table if exists T_UseCommodity;

drop table if exists T_UseCommodityDetail;

drop table if exists T_User;

drop table if exists T_UserRole;

drop table if exists T_Warehouse;

/*==============================================================*/
/* User: training                                               */
/*==============================================================*/
create user training;

/*==============================================================*/
/* Table: T_Bill                                                */
/*==============================================================*/
create table T_Bill
(
   billId               int not null auto_increment,
   studentId            int not null,
   schoolId             int not null,
   userId               int not null,
   paymentId            int,
   billType             int not null,
   billDate             date,
   pay                  double not null,
   favourable           double,
   payed                double not null,
   note                 varchar(500),
   insertDate           date,
   primary key (billId)
);

alter table T_Bill comment '消费单类型  0--退费，1--消费
';

/*==============================================================*/
/* Table: T_BillDetail                                          */
/*==============================================================*/
create table T_BillDetail
(
   billDetailId         int not null auto_increment,
   billId               int,
   billItemName         varchar(200),
   courseId             int,
   feeItemId            int,
   commodityId          int,
   warehouseId          int,
   productType          varchar(20),
   price                double,
   qty                  int,
   discount             double,
   discountAmount       double,
   unitName             varchar(50),
   status               int,
   primary key (billDetailId)
);

alter table T_BillDetail comment '收费类型: 1课程，2收费项，3 商品
状态：当收费类型是课程时，用到状态';

/*==============================================================*/
/* Table: T_Class                                               */
/*==============================================================*/
create table T_Class
(
   classId              int not null,
   className            varchar(50) not null,
   courseId             int not null,
   teacherId            int not null,
   lessons              int,
   startDate            date not null,
   timeRule             varchar(500),
   classroomId          int not null,
   courseProgress       int,
   planCount            int not null,
   stuCount             int,
   note                 varchar(500),
   schoolId             int not null,
   classType            int not null,
   endDate              date,
   arrangeLessons       int,
   lessonMinute         int,
   lessonCommission     int,
   createDate           date not null,
   createrId            int not null,
   primary key (classId)
);

/*==============================================================*/
/* Table: T_Classroom                                           */
/*==============================================================*/
create table T_Classroom
(
   classroomId          int not null,
   classroomName        varchar(50) not null,
   seating              int,
   status               int,
   primary key (classroomId)
);

alter table T_Classroom comment '状态  0 -- 禁用，1 -- 可用
';

/*==============================================================*/
/* Table: T_Commodity                                           */
/*==============================================================*/
create table T_Commodity
(
   commodityId          int not null auto_increment,
   commodityTypeId      int,
   unitId               int,
   commodityName        varchar(50) not null,
   purchasePrice        double not null,
   salePrice            double not null,
   size                 varchar(50),
   qtyStore             int not null,
   status               int not null,
   note                 varchar(500),
   primary key (commodityId)
);

/*==============================================================*/
/* Table: T_CommodityType                                       */
/*==============================================================*/
create table T_CommodityType
(
   commodityTypeId      int not null auto_increment,
   commodityTypeName    varchar(50) not null,
   status               int not null,
   primary key (commodityTypeId)
);

/*==============================================================*/
/* Table: T_Course                                              */
/*==============================================================*/
create table T_Course
(
   courseId             int not null auto_increment,
   courseTypeId         int,
   courseName           varchar(50) not null,
   courseUnit           varchar(20) not null,
   unitPrice            double not null,
   status               int not null,
   note                 varchar(500),
   primary key (courseId)
);

/*==============================================================*/
/* Table: T_CourseType                                          */
/*==============================================================*/
create table T_CourseType
(
   courseTypeId         int not null auto_increment,
   courseTypeName       varchar(50),
   primary key (courseTypeId)
);

/*==============================================================*/
/* Table: T_FeeItem                                             */
/*==============================================================*/
create table T_FeeItem
(
   feeItemId            int not null auto_increment,
   feeItemName          varchar(50),
   fee                  double,
   status               int not null,
   primary key (feeItemId)
);

/*==============================================================*/
/* Table: T_Holiday                                             */
/*==============================================================*/
create table T_Holiday
(
   holidayId            int not null,
   holidayName          varchar(50),
   startDate            date not null,
   endDate              date not null,
   primary key (holidayId)
);

/*==============================================================*/
/* Table: T_LessonDegree                                        */
/*==============================================================*/
create table T_LessonDegree
(
   lessonDegreeId       int not null,
   subject              varchar(50),
   periodTimeId         int not null,
   lessons              int not null,
   factCount            int,
   lateCount            int,
   advanceCount         int,
   truancyCount         int,
   leaveCount           int,
   lessonType           int,
   teacherId            int not null,
   lessonStatus         int,
   userId               int,
   classroomId          int,
   classId              int,
   lessonDegreeDate     date not null,
   note                 varchar(500),
   primary key (lessonDegreeId)
);

alter table T_LessonDegree comment '上课类型  0 -- 补课，1 -- 正常
上课状态  0 -- 未上课 ， 1 -- 已上课';

/*==============================================================*/
/* Table: T_Media                                               */
/*==============================================================*/
create table T_Media
(
   mediaId              int not null auto_increment,
   mediaName            varchar(50) not null,
   status               int not null,
   primary key (mediaId)
);

/*==============================================================*/
/* Table: T_Payment                                             */
/*==============================================================*/
create table T_Payment
(
   paymentId            int not null auto_increment,
   schoolId             int not null,
   studentId            int not null,
   userId               int not null,
   paymentType          int not null,
   payway               int not null,
   payMoney             double not null,
   transactionDate      date,
   note                 varchar(500),
   creditExpiration     date,
   insertTime           date,
   primary key (paymentId)
);

alter table T_Payment comment ' 帐单类型  1--交费  2--退费  3--借款  4--扣除借款  5--业务扣费  6--业务退费
';

/*==============================================================*/
/* Table: T_PeriodTime                                          */
/*==============================================================*/
create table T_PeriodTime
(
   periodTimeId         int not null,
   startTime            varchar(10) not null,
   endTime              varchar(10) not null,
   groupType            int not null,
   primary key (periodTimeId)
);

alter table T_PeriodTime comment '分组  1 -- 春，2 -- 夏，3 -- 秋，4 -- 冬，0 -- 其它
';

/*==============================================================*/
/* Table: T_PotCourse                                           */
/*==============================================================*/
create table T_PotCourse
(
   potCourseId          int not null auto_increment,
   CourseName           varchar(50) not null,
   status               int not null,
   primary key (potCourseId)
);

/*==============================================================*/
/* Table: T_Potential                                           */
/*==============================================================*/
create table T_Potential
(
   potentialId          int not null auto_increment,
   potCourseId          int,
   mediaId              int,
   userId               int,
   potentialStuStatusId int,
   schoolId             int,
   lastReplyUserId      int,
   potentialName        varchar(50) not null,
   potentialDate        date not null,
   sex                  int,
   appellation          varchar(50),
   tel                  varchar(15) not null,
   mobileTel            varchar(15) not null,
   tel1                 varchar(15),
   QQ                   varchar(15),
   email                varchar(50),
   timeRule             varchar(100),
   lastReplyDate        date,
   reCount              int not null,
   NextReplyDate        date,
   note                 varchar(500),
   insertDate           date not null,
   primary key (potentialId)
);

/*==============================================================*/
/* Table: T_PotentialStuStatus                                  */
/*==============================================================*/
create table T_PotentialStuStatus
(
   potentialStuStatusId int not null auto_increment,
   potentialStuStatusName varchar(50) not null,
   status               int not null,
   primary key (potentialStuStatusId)
);

alter table T_PotentialStuStatus comment '状态  0--禁用  1--可用
';

/*==============================================================*/
/* Table: T_RecRej                                              */
/*==============================================================*/
create table T_RecRej
(
   recRejId             int not null auto_increment,
   recRejCode           varchar(50) not null,
   recRejType           int not null,
   recRejDate           date not null,
   qtyTotal             int not null,
   amountTotal          int not null,
   note                 varchar(500),
   userId               int not null,
   supplierId           int not null,
   primary key (recRejId),
   key AK_AK (recRejCode)
);

alter table T_RecRej comment '类型 -- 进货 1  退货 -1';

/*==============================================================*/
/* Table: T_RecRejDetail                                        */
/*==============================================================*/
create table T_RecRejDetail
(
   recRejDetailId       int not null auto_increment,
   recRejId             int,
   commodityId          int,
   warehouseId          int,
   price                double,
   qty                  int,
   primary key (recRejDetailId)
);

/*==============================================================*/
/* Table: T_Reply                                               */
/*==============================================================*/
create table T_Reply
(
   replyId              int not null auto_increment,
   potentialId          int not null,
   potentialStuStatusId int not null,
   userId               int not null,
   replyDate            date not null,
   content              varchar(500) not null,
   nextReplyDate        date,
   primary key (replyId)
);

/*==============================================================*/
/* Table: T_Right                                               */
/*==============================================================*/
create table T_Right
(
   rightId              int not null auto_increment,
   rightName            varchar(50),
   rightUrl             varchar(100),
   parentRightId        int,
   isLeaf               bit,
   array                int,
   primary key (rightId)
);

/*==============================================================*/
/* Table: T_Role                                                */
/*==============================================================*/
create table T_Role
(
   roleId               int not null auto_increment,
   roleName             varchar(50),
   primary key (roleId)
);

/*==============================================================*/
/* Table: T_RoleRight                                           */
/*==============================================================*/
create table T_RoleRight
(
   rightId              int not null,
   roleId               int not null,
   state                int,
   primary key (rightId, roleId)
);

/*==============================================================*/
/* Table: T_School                                              */
/*==============================================================*/
create table T_School
(
   schoolId             int not null auto_increment,
   parentSchoolId       int,
   schoolCode           varchar(100) not null,
   schoolName           varchar(50) not null,
   tel                  varchar(30),
   address              varchar(100),
   status               int not null,
   array                int not null,
   isLeaf               bit not null,
   primary key (schoolId),
   key AK_AK (schoolCode)
);

alter table T_School comment '校区存在等级概念：
父校区，可以查看该父校区下校区的信息，是用校区编号来区分开的：
校区编号=父';

/*==============================================================*/
/* Table: T_Store                                               */
/*==============================================================*/
create table T_Store
(
   storeId              int not null auto_increment,
   commodityId          int,
   warehouseId          int,
   qtyStore             int,
   primary key (storeId)
);

/*==============================================================*/
/* Table: T_StuClass                                            */
/*==============================================================*/
create table T_StuClass
(
   stuClassId           int not null,
   studentId            int not null,
   billDetailId         int not null,
   classId              int not null,
   lessonSchoolId       int not null,
   selectSchoolId       int not null,
   userId               int not null,
   selectDate           date not null,
   lessons              int,
   courseProgress       int,
   scStatus             int,
   continueReg          int not null,
   score                double,
   opinionDate          date,
   opinion              varchar(500),
   grade                varchar(10),
   factCount            int,
   lateCount            int,
   advanceCount         int,
   truancyCount         int,
   leaveCount           int,
   fillCount            int,
   note                 varchar(500),
   insertTime           date,
   primary key (stuClassId)
);

alter table T_StuClass comment '选班状态  1 -- 正常，2 -- 插班，3 -- 转班，4 -- 休学，5 -- 退学，6 --弃学

';

/*==============================================================*/
/* Table: T_Student                                             */
/*==============================================================*/
create table T_Student
(
   studentId            int not null auto_increment,
   schoolId             int,
   mediaId              int,
   userId               int,
   studentName          varchar(50) not null,
   sex                  int,
   studentType          int not null,
   birthday             date,
   password             varchar(50) not null,
   enrollDate           date not null,
   tel                  varchar(15) not null,
   mobileTel            varchar(15) not null,
   tel1                 varchar(15),
   QQ                   varchar(15),
   email                varchar(50),
   address              varchar(50),
   postCode             varchar(50),
   IDcard               varchar(30),
   diploma              varchar(0),
   NextReplyDate        date,
   note                 varchar(500),
   consumedMoney        double,
   insertDate           date not null,
   billCount            int,
   arrearageMoney       double,
   availableMoney       double,
   creditExpiration     date,
   creditRemark         varchar(500),
   primary key (studentId)
);

alter table T_Student comment '学员类型 0--学生  1--上班族
';

/*==============================================================*/
/* Table: T_Supplier                                            */
/*==============================================================*/
create table T_Supplier
(
   supplierId           int not null auto_increment,
   supplierName         varchar(50) not null,
   linkMan              varchar(50),
   tel                  varchar(15),
   address              varchar(50),
   status               int not null,
   primary key (supplierId)
);

/*==============================================================*/
/* Table: T_SystemConfig                                        */
/*==============================================================*/
create table T_SystemConfig
(
   systemConfigId       int not null auto_increment,
   systemName           varchar(100),
   classType            varchar(20),
   primary key (systemConfigId)
);

alter table T_SystemConfig comment '班级模式：
1 --- 一对多模式（以"学期“为单位）
2 --- 一对多模式（以”课时“为单位';

/*==============================================================*/
/* Table: T_Unit                                                */
/*==============================================================*/
create table T_Unit
(
   unitId               int not null auto_increment,
   unitName             varchar(50) not null,
   status               int not null,
   primary key (unitId)
);

/*==============================================================*/
/* Table: T_UseCommodity                                        */
/*==============================================================*/
create table T_UseCommodity
(
   useCommodityId       int not null auto_increment,
   userId               int not null,
   useDate              date not null,
   qtyTotal             int not null,
   note                 varchar(500),
   handlerId            int not null,
   primary key (useCommodityId)
);

/*==============================================================*/
/* Table: T_UseCommodityDetail                                  */
/*==============================================================*/
create table T_UseCommodityDetail
(
   useCommodityDetailId int not null auto_increment,
   useCommodityId       int not null,
   commodityId          int not null,
   warehouseId          int not null,
   qty                  int not null,
   isNeedReturn         int not null,
   returnDate           date,
   returnStatus         int,
   primary key (useCommodityDetailId)
);

alter table T_UseCommodityDetail comment '是否需要归还 1 -- 需要归还 0 --- 不需要归还
归还状态 1 -- 未还 0 -- 已还';

/*==============================================================*/
/* Table: T_User                                                */
/*==============================================================*/
create table T_User
(
   userId               int not null auto_increment,
   schoolId             int not null,
   userCode             varchar(50) not null,
   userName             varchar(50) not null,
   userPwd              varchar(50) not null,
   sex                  int not null,
   birthday             date not null,
   isPartTime           int not null,
   IsTeacher            int not null,
   comeDate             date not null,
   outDate              date,
   course               varchar(50),
   finishSchool         varchar(50),
   diploma              varchar(50),
   resume               varchar(1000),
   tel                  varchar(15),
   email                varchar(50),
   address              varchar(100),
   postCode             varchar(10),
   headship             varchar(50),
   IDcard               varchar(30),
   basePay              double,
   hourFee              double,
   status               int not null,
   primary key (userId),
   key AK_UQ_USERCODE_T_USER (userCode)
);

alter table T_User comment '系统用户表，员工表
性别--- 0女，1男
是否兼职。（0否，1是）
是否授课。（0';

/*==============================================================*/
/* Table: T_UserRole                                            */
/*==============================================================*/
create table T_UserRole
(
   userId               int not null,
   roleId               int not null,
   primary key (userId, roleId)
);

/*==============================================================*/
/* Table: T_Warehouse                                           */
/*==============================================================*/
create table T_Warehouse
(
   warehouseId          int not null auto_increment,
   warehouseName        varchar(50) not null,
   tel                  varchar(15),
   address              varchar(50),
   schoolId             int not null,
   status               int not null,
   primary key (warehouseId)
);

alter table T_Bill add constraint FK_Reference_37 foreign key (studentId)
      references T_Student (studentId) on delete restrict on update restrict;

alter table T_Bill add constraint FK_Reference_38 foreign key (schoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_Bill add constraint FK_Reference_39 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_Bill add constraint FK_Reference_48 foreign key (paymentId)
      references T_Payment (paymentId) on delete restrict on update restrict;

alter table T_BillDetail add constraint FK_Reference_40 foreign key (billId)
      references T_Bill (billId) on delete restrict on update restrict;

alter table T_BillDetail add constraint FK_Reference_41 foreign key (courseId)
      references T_Course (courseId) on delete restrict on update restrict;

alter table T_BillDetail add constraint FK_Reference_42 foreign key (feeItemId)
      references T_FeeItem (feeItemId) on delete restrict on update restrict;

alter table T_BillDetail add constraint FK_Reference_43 foreign key (commodityId)
      references T_Commodity (commodityId) on delete restrict on update restrict;

alter table T_BillDetail add constraint FK_Reference_44 foreign key (warehouseId)
      references T_Warehouse (warehouseId) on delete restrict on update restrict;

alter table T_Class add constraint FK_Reference_49 foreign key (schoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_Class add constraint FK_Reference_50 foreign key (classroomId)
      references T_Classroom (classroomId) on delete restrict on update restrict;

alter table T_Class add constraint FK_Reference_56 foreign key (courseId)
      references T_Course (courseId) on delete restrict on update restrict;

alter table T_Class add constraint FK_Reference_57 foreign key (teacherId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_Commodity add constraint FK_Reference_12 foreign key (commodityTypeId)
      references T_CommodityType (commodityTypeId) on delete restrict on update restrict;

alter table T_Commodity add constraint FK_Reference_13 foreign key (unitId)
      references T_Unit (unitId) on delete restrict on update restrict;

alter table T_Course add constraint FK_Reference_8 foreign key (courseTypeId)
      references T_CourseType (courseTypeId) on delete restrict on update restrict;

alter table T_LessonDegree add constraint FK_Reference_51 foreign key (teacherId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_LessonDegree add constraint FK_Reference_52 foreign key (periodTimeId)
      references T_PeriodTime (periodTimeId) on delete restrict on update restrict;

alter table T_LessonDegree add constraint FK_Reference_53 foreign key (classroomId)
      references T_Classroom (classroomId) on delete restrict on update restrict;

alter table T_LessonDegree add constraint FK_Reference_54 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_LessonDegree add constraint FK_Reference_55 foreign key (classId)
      references T_Class (classId) on delete restrict on update restrict;

alter table T_Payment add constraint FK_Reference_45 foreign key (studentId)
      references T_Student (studentId) on delete restrict on update restrict;

alter table T_Payment add constraint FK_Reference_46 foreign key (schoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_Payment add constraint FK_Reference_47 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_Potential add constraint FK_Reference_25 foreign key (potCourseId)
      references T_PotCourse (potCourseId) on delete restrict on update restrict;

alter table T_Potential add constraint FK_Reference_26 foreign key (mediaId)
      references T_Media (mediaId) on delete restrict on update restrict;

alter table T_Potential add constraint FK_Reference_27 foreign key (potentialStuStatusId)
      references T_PotentialStuStatus (potentialStuStatusId) on delete restrict on update restrict;

alter table T_Potential add constraint FK_Reference_28 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_Potential add constraint FK_Reference_29 foreign key (schoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_Potential add constraint FK_Reference_30 foreign key (lastReplyUserId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_RecRej add constraint FK_Reference_10 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_RecRej add constraint FK_Reference_11 foreign key (supplierId)
      references T_Supplier (supplierId) on delete restrict on update restrict;

alter table T_RecRejDetail add constraint FK_Reference_14 foreign key (recRejId)
      references T_RecRej (recRejId) on delete restrict on update restrict;

alter table T_RecRejDetail add constraint FK_Reference_15 foreign key (commodityId)
      references T_Commodity (commodityId) on delete restrict on update restrict;

alter table T_RecRejDetail add constraint FK_Reference_16 foreign key (warehouseId)
      references T_Warehouse (warehouseId) on delete restrict on update restrict;

alter table T_Reply add constraint FK_Reference_31 foreign key (potentialId)
      references T_Potential (potentialId) on delete restrict on update restrict;

alter table T_Reply add constraint FK_Reference_32 foreign key (potentialStuStatusId)
      references T_PotentialStuStatus (potentialStuStatusId) on delete restrict on update restrict;

alter table T_Reply add constraint FK_Reference_33 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_Right add constraint FK_Reference_1 foreign key (parentRightId)
      references T_Right (rightId) on delete restrict on update restrict;

alter table T_RoleRight add constraint FK_Reference_2 foreign key (rightId)
      references T_Right (rightId) on delete restrict on update restrict;

alter table T_RoleRight add constraint FK_Reference_3 foreign key (roleId)
      references T_Role (roleId) on delete restrict on update restrict;

alter table T_School add constraint FK_Reference_6 foreign key (parentSchoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_Store add constraint FK_Reference_17 foreign key (commodityId)
      references T_Commodity (commodityId) on delete restrict on update restrict;

alter table T_Store add constraint FK_Reference_18 foreign key (warehouseId)
      references T_Warehouse (warehouseId) on delete restrict on update restrict;

alter table T_StuClass add constraint FK_Reference_58 foreign key (studentId)
      references T_Student (studentId) on delete restrict on update restrict;

alter table T_StuClass add constraint FK_Reference_59 foreign key (billDetailId)
      references T_BillDetail (billDetailId) on delete restrict on update restrict;

alter table T_StuClass add constraint FK_Reference_60 foreign key (classId)
      references T_Class (classId) on delete restrict on update restrict;

alter table T_StuClass add constraint FK_Reference_61 foreign key (lessonSchoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_StuClass add constraint FK_Reference_62 foreign key (selectSchoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_StuClass add constraint FK_Reference_63 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_Student add constraint FK_Reference_34 foreign key (schoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_Student add constraint FK_Reference_35 foreign key (mediaId)
      references T_Media (mediaId) on delete restrict on update restrict;

alter table T_Student add constraint FK_Reference_36 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_UseCommodity add constraint FK_Reference_20 foreign key (handlerId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_UseCommodity add constraint FK_Reference_21 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_UseCommodityDetail add constraint FK_Reference_22 foreign key (useCommodityId)
      references T_UseCommodity (useCommodityId) on delete restrict on update restrict;

alter table T_UseCommodityDetail add constraint FK_Reference_23 foreign key (commodityId)
      references T_Commodity (commodityId) on delete restrict on update restrict;

alter table T_UseCommodityDetail add constraint FK_Reference_24 foreign key (warehouseId)
      references T_Warehouse (warehouseId) on delete restrict on update restrict;

alter table T_User add constraint FK_Reference_7 foreign key (schoolId)
      references T_School (schoolId) on delete restrict on update restrict;

alter table T_UserRole add constraint FK_Reference_4 foreign key (roleId)
      references T_Role (roleId) on delete restrict on update restrict;

alter table T_UserRole add constraint FK_Reference_5 foreign key (userId)
      references T_User (userId) on delete restrict on update restrict;

alter table T_Warehouse add constraint FK_Reference_9 foreign key (schoolId)
      references T_School (schoolId) on delete restrict on update restrict;

