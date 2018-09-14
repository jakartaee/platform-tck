 DROP TABLE caller ;
 DROP TABLE caller_groups ;

 CREATE TABLE caller(name VARCHAR(64) PRIMARY KEY, password VARCHAR(1024)) ;
 CREATE TABLE caller_groups(caller_name VARCHAR(64), group_name VARCHAR(64)) ;

 INSERT INTO caller VALUES('tom', 'secret1') ;
 INSERT INTO caller VALUES('emma', 'secret2') ;
 INSERT INTO caller VALUES('bob', 'secret3') ;
 INSERT INTO caller VALUES('tom_hash512_saltsize16', 'PBKDF2WithHmacSHA512:1024:DbjXqT9p8VhJ7OtU6DrqDw==:p/qihG8IZKkz03JzKd6XXA==') ;
 INSERT INTO caller VALUES('tom_hash256_saltsize32', 'PBKDF2WithHmacSHA256:2048:suVayUIJMQMc6wCgckvAIgKRlo1UkxyFXhXbTxX6C7s=:cvdHkBXVUCN2WL3LRAYodeCdNZxEM4RLlNCCYP68Kmg=') ;
 INSERT INTO caller VALUES('tom_hash512_saltsize32', 'PBKDF2WithHmacSHA512:2048:dPTjUfiklfyg2bas/KOQKqEfdtoXK8YvbBscIxA8tNg=:ixBg0wr3ySBI86y8HP7+Yw==') ;
 
 INSERT INTO caller_groups VALUES('tom', 'Administrator') ;
 INSERT INTO caller_groups VALUES('tom', 'Manager') ;

 INSERT INTO caller_groups VALUES('tom_hash512_saltsize16', 'Administrator') ;
 INSERT INTO caller_groups VALUES('tom_hash512_saltsize16', 'Manager') ;
 
 INSERT INTO caller_groups VALUES('tom_hash256_saltsize32', 'Administrator') ;
 INSERT INTO caller_groups VALUES('tom_hash256_saltsize32', 'Manager') ;

 INSERT INTO caller_groups VALUES('tom_hash512_saltsize32', 'Administrator') ;
 INSERT INTO caller_groups VALUES('tom_hash512_saltsize32', 'Manager') ;

 INSERT INTO caller_groups VALUES('emma', 'Administrator') ;
 INSERT INTO caller_groups VALUES('emma', 'Employee') ;

 INSERT INTO caller_groups VALUES('bob', 'Administrator') ;
