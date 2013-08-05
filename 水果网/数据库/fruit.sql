/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2013/7/15 16:56:34                           */
/*==============================================================*/


drop table if exists t_classify;

drop table if exists t_dept;

drop table if exists t_fruit;

drop table if exists t_fruitattr;

drop table if exists t_func;

drop table if exists t_order;

drop table if exists t_orderdetail;

drop table if exists t_resources;

drop table if exists t_role;

drop table if exists t_rolefunc;

drop table if exists t_user;

drop table if exists t_userdept;

drop table if exists t_userrole;

/*==============================================================*/
/* Table: t_classify                                            */
/*==============================================================*/
create table t_classify
(
   classifyid           int(10) not null auto_increment,
   name                 varchar(256),
   remark               varchar(512),
   rstatus              int(2) default 0,
   intime               datetime,
   primary key (classifyid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_dept                                                */
/*==============================================================*/
create table t_dept
(
   deptid               int(10) not null auto_increment,
   deptname             varchar(16),
   status               int(2) default 0,
   rstatus              int(2) default 0,
   intime               datetime,
   primary key (deptid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_fruit                                               */
/*==============================================================*/
create table t_fruit
(
   fruitid              int(10) not null auto_increment,
   name                 varchar(256),
   remark               varchar(512),
   intime               datetime,
   price                varchar(32),
   spdw                 int(2) default 1,
   kc                   int(10) default 0,
   psfw                 varchar(128),
   cd                   varchar(128),
   pp                   varchar(128),
   pfruitid             int(10),
   status               int(2) default 1,
   rstatus              int(2) default 0,
   primary key (fruitid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

alter table t_fruit comment 'spdw：1、500g   2、箱   3、盒
status：1、未发布  0、已发布';

/*==============================================================*/
/* Table: t_fruitattr                                           */
/*==============================================================*/
create table t_fruitattr
(
   fruidattrid          int(10) not null auto_increment,
   attrname             varchar(64),
   attrkey              varchar(64),
   attrvalue            text,
   fruitid              int(10),
   primary key (fruidattrid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_func                                                */
/*==============================================================*/
create table t_func
(
   funcid               int(10) not null auto_increment,
   funcname             varchar(16),
   status               int(2) default 0,
   rstatus              int(2) default 0,
   intime               datetime,
   funcurl              varchar(64),
   funckey              varchar(32),
   pfuncid              int(10),
   primary key (funcid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_order                                               */
/*==============================================================*/
create table t_order
(
   orderid              int(10) not null auto_increment,
   shr                  varchar(64),
   shdz                 varchar(512),
   shsj                 datetime,
   sjhm                 varchar(32),
   status               int(2) default 0,
   rstatus              int(2) default 0,
   primary key (orderid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

alter table t_order comment 'status：0、未配送   1、正在配送   2、配送完毕';

/*==============================================================*/
/* Table: t_orderdetail                                         */
/*==============================================================*/
create table t_orderdetail
(
   orderdetailid        int(10) not null auto_increment,
   fruitid              int(10),
   fruitnum             int(10) default 0,
   rstatus              int(2) default 0,
   primary key (orderdetailid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_resources                                           */
/*==============================================================*/
create table t_resources
(
   resourceid           int(10) not null auto_increment,
   objid                int(10),
   objtype              int(2),
   rstatus              int(2) default 0,
   type                 int(2),
   intime               datetime,
   objurl               varchar(256),
   name                 varchar(128),
   primary key (resourceid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

alter table t_resources comment 'objtype 1、水果
type 1、图片 2、视频  3、音频 ';

/*==============================================================*/
/* Table: t_role                                                */
/*==============================================================*/
create table t_role
(
   roleid               int(10) not null auto_increment,
   rolename             varchar(16),
   status               int(2) default 0,
   rstatus              int(2) default 0,
   intime               datetime,
   primary key (roleid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_rolefunc                                            */
/*==============================================================*/
create table t_rolefunc
(
   roleid               int(10),
   funcid               int(10),
   rolefuncid           int(10) not null,
   primary key (rolefuncid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_user                                                */
/*==============================================================*/
create table t_user
(
   userid               int(10) not null auto_increment,
   username             varchar(16),
   password             varchar(16),
   realname             varchar(32),
   status               int(2) default 0,
   rstatus              int(2) default 0,
   intime               datetime,
   primary key (userid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_userdept                                            */
/*==============================================================*/
create table t_userdept
(
   deptid               int(10),
   userid               int(10),
   userdeptid           int(10) not null,
   primary key (userdeptid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

/*==============================================================*/
/* Table: t_userrole                                            */
/*==============================================================*/
create table t_userrole
(
   roleid               int(10),
   userid               int(10),
   userroleid           int(10) not null,
   primary key (userroleid)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

