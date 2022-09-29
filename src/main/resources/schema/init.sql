create table dictionary (
	id serial primary key,
	value varchar(255) unique,
	frequency int default(0),
	check(frequency >= 0)
)