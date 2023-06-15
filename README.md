# food-recommender
Back-end untuk menghasilkan rekomendasi berdasarkan karakteristik kegemaran bahan makanan serta tingkat kepedasan pengguna

Inbound port: 8000 (FastAPI)

Route yang tersedia:

'/predict' (POST): menerima data user (terutama field "spiceLevel" dan "likedIngredients") dan mengembalikan uid dan list food_id hasil rekomendasi

Contoh request body request: 
{

    "_id" : "648573c85d03c2e375114fc7",
    
    "name": "Joshua Adams",
    
    "spiceLevel": "A little bit spicy",
    
    "likedIngredients": ["beef","pepperoni", "chicken", "tomatoes", "sauce", "peppers", "mushroom"]
    
}

Contoh return:
{

    "uid": "648573c85d03c2e375114fc7",
    
    "recommendation": [
    
        "648573435d03c2e375114fba",
        
        "648573455d03c2e375114fc3",
        
        "648573435d03c2e375114fb9",
        
        "648573445d03c2e375114fbe",
        
        "648573445d03c2e375114fbf",
        
        "648573425d03c2e375114fb7"
        
    ]
    
}
