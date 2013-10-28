create user reportu identified by '123';
create database reportdb;
grant all privileges on reportdb.* to reportu@'%' identified by '123';
flush privileges;
use reportdb;

create table computer(osname char(100),
osarch char(20),
osversion char(40),
username char(100),
ip char(16),
mac char(12) not null primary key,
name char(50),
age char(10),
position char(100),
department char(100),
gender char(20)
);

create table bigdata(time char(50),
source char(100),
destination char(100),
datasize int,
type char(50)
);

create table event(computerid char(50),
eventid char(200),time char(50),
description text,
exception char(10),
primary key(computerid,eventid,time)
);

create table processblacklist(
name char(100),
description text
);

create table processlist(computerid char(50),
pid char(10),
name char(100),
primary key(computerid,pid)
);

create table commandlist(cmd char(30),
par char(100),
computerID char(12),
foreign key (computerID)  references computer(mac)
);


