CREATE TABLE IF NOT EXISTS ScanProfileHib (
   id int NOT NULL PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   daterange VARCHAR(100) NULL,
   evlimit int NULL,
   logtype VARCHAR(100) NULL,
   sortingProp VARCHAR(100) NULL,
   groupType VARCHAR(100) NULL,
   rgxexp VARCHAR(2000) NULL,
   rgxop VARCHAR(100) NULL,
   userstartdate VARCHAR(100) NULL,
   userenddate VARCHAR(100) NULL,
);

CREATE TABLE IF NOT EXISTS LOGSPATHHIB (
   profile_id int NOT NULL,
   id int NOT NULL,
   Path VARCHAR(10000) NOT NULL,
   Server VARCHAR(10000) NOT NULL,
   CONSTRAINT pk_id PRIMARY KEY (id),
   CONSTRAINT fk_path FOREIGN KEY (profile_id) REFERENCES ScanProfileHib(id)
);

CREATE TABLE IF NOT EXISTS regexphib (
  profile_id int NOT NULL,
  id int NOT NULL PRIMARY KEY,
  eventtype VARCHAR(100) NULL,
  CONSTRAINT fk_regexp FOREIGN KEY (profile_id) REFERENCES ScanProfileHib(id)
);

CREATE TABLE IF NOT EXISTS filterhib (
  id int NOT NULL PRIMARY KEY,
);

CREATE TABLE IF NOT EXISTS filtervals (
  filter_id int NOT NULL PRIMARY KEY,
  filterval VARCHAR(10000) NOT NULL,
  CONSTRAINT fk_filterhib FOREIGN KEY (filter_id) REFERENCES filterhib(id)
);
