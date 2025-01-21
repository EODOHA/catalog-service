CREATE TABLE book (
	id					BIGSERIAL PRIMARY KEY NOT NULL, /* 테이블의 기본키, 연속 숫자 생성.*/
	author				varchar(255) NOT NULL,
	isbn				varchar(255) UNIQUE NOT NULL,
	price				float8 NOT NULL,
	title				varchar(255) NOT NULL,
	created_date		timestamp NOT NULL,
	last_modified_date	timestamp NOT NULL,
	version				integer NOT NULL
);