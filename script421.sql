alter table students add constraint age_const check (age > 16);
alter table students add constraint name set not null;
alter table students add constraint nameUnique unique (name);
alter table faculties add constraint nameColorUnique unique (name, color);
alter table students add constraint age set default 20;