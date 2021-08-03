-----------------------------------------------SQL DUMP FOR database1--------------------------------------------
create table table_a(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_a(id,Name,Company,code) VALUES("1","monitor","XYZ","1234");
INSERT INTO table_a(id,Name,Company,code) VALUES("2","laptop","abc","123U");

create table table_b(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_b(id,Name,Company,code) VALUES("1","bmw","XYZ","1234");
INSERT INTO table_b(id,Name,Company,code) VALUES("2","ferrari","MNO","123U");

create table table_c(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));


create table table_d(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_d(id,Name,Company,code) VALUES("1","Maruti","XYZ","1234");
INSERT INTO table_d(id,Name,Company,code) VALUES("2","Honda","MNO","123U");

create table table_e(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_e(id,Name,Company,code) VALUES("1","Apple","XYZ","1234");
INSERT INTO table_e(id,Name,Company,code) VALUES("2","Samsung","MNO","123U");

create table user_data1 (user_id INT NOT_NULL,user_name VARCHAR NOT_NULL,user_surname VARCHAR NOT_NULL,user_email VARCHAR NOT_NULL,user_contact INT NOT_NULL);
INSERT INTO user_data1(user_id,user_name,user_surname,user_email,user_contact) VALUES("5","nicola","tesla","nicola.tesla@gmail.com","5566223311");
INSERT INTO user_data1(user_id,user_name,user_surname,user_email,user_contact) VALUES("6","elon","musk","elonm@gmail.com","8844556622");
INSERT INTO user_data1(user_id,user_name,user_surname,user_email,user_contact) VALUES("7","jeff","bezos","jeff.bezos@gmail.com","4455663322");
INSERT INTO user_data1(user_id,user_name,user_surname,user_email,user_contact) VALUES("8","bill","gates","bill.gates@gmail.com","4455663322");

create table table_f(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_f(id,Name,Company,code) VALUES("1","USA","XYZ","1234");
INSERT INTO table_f(id,Name,Company,code) VALUES("2","Canada","MNO","123U");

create table table_g(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_g(id,Name,Company,code) VALUES("1","India","XYZ","1234");
INSERT INTO table_g(id,Name,Company,code) VALUES("2","China","MNO","123U");

create table table_h(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_h(id,Name,Company,code) VALUES("1","Air India","XYZ","1234");
INSERT INTO table_h(id,Name,Company,code) VALUES("2","Air Canada","MNO","123U");

create table table_i(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_i(id,Name,Company,code) VALUES("1","NASA","XYZ","1234");
INSERT INTO table_i(id,Name,Company,code) VALUES("2","ISRO","MNO","123U");
INSERT INTO table_i(id,Name,Company,code) VALUES("3","ISRO","MNO","123U");
INSERT INTO table_i(id,Name,Company,code) VALUES("4","ISRO","MNO","123U");

create table table_j(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_j(id,Name,Company,code) VALUES("1","Walmart","XYZ","1234");
INSERT INTO table_j(id,Name,Company,code) VALUES("2","Best Buy","MNO","123U");
INSERT INTO table_j(id,Name,Company,code) VALUES("3","Best Buy","MNO","123U");
INSERT INTO table_j(id,Name,Company,code) VALUES("4","Best Buy","MNO","123U");

create table table_k(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_k(id,Name,Company,code) VALUES("1","Lenovo","XYZ","1234");
INSERT INTO table_k(id,Name,Company,code) VALUES("2","HP","MNO","123U");

create table table_l(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_l(id,Name,Company,code) VALUES("1","Dalhousie University","XYZ","1234");
INSERT INTO table_l(id,Name,Company,code) VALUES("2","university Of Toronto","MNO","123U");

create table user_data (user_id INT NOT_NULL,user_name VARCHAR NOT_NULL,user_surname VARCHAR NOT_NULL,user_email VARCHAR NOT_NULL,user_contact INT NOT_NULL);
INSERT INTO user_data(user_id,user_name,user_surname,user_email,user_contact) VALUES("1","Mukesh","Ambani","Mukesh.Ambani@gmail.com","5566223311");
INSERT INTO user_data(user_id,user_name,user_surname,user_email,user_contact) VALUES("2","Ratan","Tata","RatanT@gmail.com","8844556622");
INSERT INTO user_data(user_id,user_name,user_surname,user_email,user_contact) VALUES("3","Narayan","Murthy","Narayan.Murthy@gmail.com","4455663322");
INSERT INTO user_data(user_id,user_name,user_surname,user_email,user_contact) VALUES("4","Azim","Premji","Azim.Premji@gmail.com","4455663322");

create table table_m(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));
INSERT INTO table_m(id,Name,Company,code) VALUES("1","Computer Science","XYZ","1234");
INSERT INTO table_m(id,Name,Company,code) VALUES("2","Applied Computer Science","MNO","123U");

create table table_n(code varchar NOT_NULL,name varchar NOT_NULL,PRIMARY_KEY (code));
INSERT INTO table_n(code,name) VALUES("1234","alpha");
INSERT INTO table_n(code,name) VALUES("123U","beta");

create table table_o(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id),CONSTRAINT fk_customer FOREIGN_KEY (code) REFERENCES table_n(code));

create table table_p(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR,PRIMARY_KEY (id));

create table table_q(id INT NOT_NULL ,Name VARCHAR NOT_NULL,Company VARCHAR NOT_NULL,code VARCHAR);
