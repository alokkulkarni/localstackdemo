#!/bin/bash
awslocal secretsmanager create-secret --region us-east-1 --name /secret/spring-boot-app --secret-string '{"property1": "property1-value", "property2": "property2-value"}'
#awslocal secretsmanager create-secret --region us-east-1 --name /secret/db-mysql-credential --secret-string '{"dbuser": "user1", "dbpassword": "password"}'
awslocal secretsmanager create-secret --name /secret/db-mysql-credential --secret-string '{"dbuser": "user1", "dbpassword": "password"}'