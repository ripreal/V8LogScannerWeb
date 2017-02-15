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

CREATE TABLE IF NOT EXISTS logpaths (
   profile_id int NOT NULL,
   path VARCHAR(10000) NOT NULL,
   CONSTRAINT pk_idpath PRIMARY KEY (profile_id, path),
   CONSTRAINT fk_path FOREIGN KEY (profile_id) REFERENCES ScanProfileHib(id)
);

CREATE TABLE IF NOT EXISTS regexphib (
  id int NOT NULL PRIMARY KEY,
  profile_id int NOT NULL,
  eventType VARCHAR(100),
  CONSTRAINT fk_regexp FOREIGN KEY (profile_id) REFERENCES ScanProfileHib(id)
)