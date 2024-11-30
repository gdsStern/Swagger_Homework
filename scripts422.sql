create table person (
	id bigint not null primary key,
	name character varying(255) unique not null,
	age integer not null check (age >= 18),
	lic boolean,
	carId bigint references car (id)
	);
create table car (
	id bigint not null primary key,
	brand character varying(255) not null,
	model character varying(255) not null,
	cost money
	);
