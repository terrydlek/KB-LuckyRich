use kb_final;

create table user(
    user_id int auto_increment primary key,
    user_name varchar(30) not null,
    email varchar(100) not null,
    gender varchar(30),
    age int,
    provider_id int,
    foreign key (provider_id) references auth_provider(provider_id)
);

create table auth_provider(
    provider_id int auto_increment primary key,
    provider_name varchar(50)
);

create table bank(
    bank_id int auto_increment primary key,
    bank_name varchar(50)
);

create table account_type(
    account_type_id int auto_increment primary key,
    account_type varchar(50) not null
);

create table account(
    account_id int auto_increment primary key,
    account_number varchar(20) not null,
    user_id int,
    bank_id int,
    account_type_id int,
    balance decimal(15) default 0,
    foreign key (user_id) references user(user_id),
    foreign key (bank_id) references bank(bank_id),
    foreign key (account_type_id) references account_type(account_type_id)
);

create table transaction(
    transaction_id int auto_increment primary key,
    account_id int,
    amount decimal(15) not null,
    transaction_type varchar(50) not null,
    category varchar(50),
    description varchar(100),
    transaction_date timestamp not null,
    foreign key (account_id) references account(account_id)
);

create table car(
    car_id int auto_increment primary key,
    car_name varchar(100),
    value int
);

/*create table user_auth(
    user_auth_id int auto_increment primary key,
    user_id int,
    provider_id int,
    access_token varchar(255) not null,
    refresh_token varchar(255) not null,
    expires_in int not null,
    foreign key (user_id) references user(user_id),
    foreign key (provider_id) references auth_provider(provider_id)
);
*/
