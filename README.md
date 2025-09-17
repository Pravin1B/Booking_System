# Booking_System

# Authentication
- JWT-based login

Use the /auth/login endpoint to get a token.
Pass the token in the header for secured APIs:
Authorization: Bearer <your_token>

# Authentication
POST /auth/login → Login & get JWT token

# Resources
POST /resources → Create a new resource (Admin only)
GET /resources → Get all resources (supports pagination & filtering)
Example: /resources?page=0&size=5&sort=id,asc
GET /resources/{id} → Get resource by ID
PUT /resources/{id} → Update resource details (Admin only)
DELETE /resources/{id} → Delete resource (Admin only)

# Reservations

POST /reservations → Create a reservation for a resource (User/Admin)
GET /reservations → Get all reservations (supports pagination & filtering)
Example: /reservations?page=0&size=5&sort=date,desc
GET /reservations/{id} → Get reservation by ID
PUT /reservations/{id} → Update reservation (User who created it / Admin)
DELETE /reservations/{id} → Cancel reservation (User who created it / Admin)

# Test Credentials
Admin → admin/admin123
User → user/user123
