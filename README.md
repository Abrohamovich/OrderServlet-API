# Order-Servlet API

## Description
This project is a simple API for managing orders. The API allows creating, retrieving, updating, and deleting orders, as well as managing the list of products in each order.

## API Endpoints

### Create an Order
**POST /order**
- Description: Creates a new order with products list.
- Input: JSON object representing an order:
```
{
    "date": "2025-02-01",
    "products": [
        {
            "name": "{{$randomProduct}}",
            "cost": {{$randomPrice}}
        }
    ]
}
```
- Output: JSON object of the created order with an assigned `id:
```
{
    "id": 2,
    "date": "2025-02-01",
    "cost": 447.67,
    "products": [
        {
            "id": 2,
            "name": "Keyboard",
            "cost": 447.67
        }
    ]
}
```

### Retrieve an Order
**GET /order?id={id}**
- Description: Retrieves an order by `id` or all orders.
- Input `id` parameter in the URL or :
```
http://localhost:9090/jakartaee-project/orders
```
```
http://localhost:9090/jakartaee-project/orders?id=1
```
- Output: JSON object representing the order:
```
[
    {
        "id": 1,
        "date": "2025-02-01",
        "cost": 800.9,
        "products": [
            {
                "id": 1,
                "name": "Shoes",
                "cost": 800.9
            }
        ]
    },
    {
        "id": 2,
        "date": "2025-02-01",
        "cost": 447.67,
        "products": [
            {
                "id": 2,
                "name": "Keyboard",
                "cost": 447.67
            }
        ]
    }
]
```
```
{
    "id": 1,
    "date": "2025-02-01",
    "cost": 800.9,
    "products": [
        {
            "id": 1,
            "name": "Shoes",
            "cost": 800.9
        }
    ]
}
```

### Update an Order
**PUT /order**
- Description: Updates an existing order.
- Input: JSON object of the order with an existing `id`:
```
{
    "id": 1,
    "date": "2025-02-02",
    "products": [
        {
            "name": "{{$randomProduct}}",
            "cost": {{$randomPrice}}
        },
        {
            "name": "{{$randomProduct}}",
            "cost": {{$randomPrice}}
        }
    ]
}
```
- Output: JSON object of the updated order:
```
{
    "id": 1,
    "date": "2025-02-01",
    "cost": 612.03,
    "products": [
        {
            "id": 4,
            "name": "Pants",
            "cost": 330.54
        },
        {
            "id": 5,
            "name": "Pants",
            "cost": 281.49
        }
    ]
}
```

### Delete an Order
**DELETE /order?id={id}**
- Description: Deletes an order by `id`.
- Input: `id` parameter in the URL:
```
http://localhost:9090/jakartaee-project/orders?id=1
```
- Output: None.

## Running the Project
1. Compile and start the server.
2. Use tools like Postman or cURL to test the API.