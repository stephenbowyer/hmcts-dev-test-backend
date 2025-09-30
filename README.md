# HMCTS Dev Test Backend

You can try the [hosted online demo of the API here](http://peter.widearea.org:4000/api).

## Features

### /api

GET list of API endpoints

### /api/task

POST to create a new task
GET to retrieve a details of all tasks

### /api/task/{id}

GET to rectrive details of one task
PATCH to update details of a task
DELETE to remove a task

### /api/task/status

GET summarised list of task status types

## Minimum Installation Requirements

This has been tested and works with OpenJDK 21 and PostgreSQL 14.

## Installation Instructions

A database must be created with the following table:

```
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2048) NULL,
    status VARCHAR(50) NOT NULL,
    due_date TIMESTAMP NOT NULL
);
zzzz

The database details are passed to the backend through the following environment variables: DB_HOST, DB_PORT, DB_NAME, DB_USER_NAME and DB_PASSWORD.

To compile and run the backend API:

```
git clone https://github.com/stephenbowyer/hmcts-dev-test-backend
cd hmcts-dev-test-backend
./gradlew build clean
./gradlew bootRun
```
