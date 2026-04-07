# Energy Management System

A Containerized Microservices Architecture for measuring and monitoring the energy consumption of different devices of users.

This project demonstrates a basic microservices architecture built using Spring Boot, where two independent services — Person and Device — communicate via REST Template.

Key Features:

Service decomposition
Inter-service communication using HTTP
Data synchronization between microservices
Containerized deployment with Docker

The system consists of:

- Person Microservice
  Manages person-related data
  Exposes REST endpoints for CRUD operations

- Device Microservice
  Manages devices
  Associates devices with persons
  Fetches person data via REST calls
