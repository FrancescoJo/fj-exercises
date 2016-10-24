# AppDeploySite
*This project is obsolete. Only for practing use.*

App deployment automation helper site (for CI)

## Prerequisites
* Java 1.8+
* Apache maven 3.0+

## Environment value setup
	# Mandatory values
	APPDEPLOY_DB_USER			; User name of database owner
	APPDEPLOY_DB_PASSWORD		; User name of database password

	# Omittable values
	APPDEPLOY_HOME				; Absolute filesystem path where database and configuration files are saved.
	APPDEPLOY_ADMIN_ENABLED		; Flag value to turn admin mode on/off. Either true/1/y or false/0/n are accepted.

## Remarks
* The embedded database file `appdeploy.h2` will be stored on running user's home directory if environment value `APPDEPLOY_HOME` is not defined.

## How to run
	mvn clean package spring-boot:run -Drun.arguments="-m START"
