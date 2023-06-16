import numpy as np
import pandas as pd
import os
from pymongo import MongoClient
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
from bson.objectid import ObjectId
from dotenv import load_dotenv

from bson import ObjectId
import pymongo
import pandas as pd

def get_menu_data(conn, database_name, collection_name):
    # Koneksi ke MongoDB
    client = pymongo.MongoClient(conn)
    db = client[database_name]
    collection = db[collection_name]

    # Ambil semua item food dari database
    items = collection.find()

    result = {}
    data = []
    
    # Daftar bahan dan kolom yang sesuai
    ingredients_dict = {
        "pepperoni": "pepperoni",
        "daging": "daging",
        "ayam": "ayam",
        "tomat": "tomat",
        "paprika": "paprika",
        "saus": "saus",
        "merica": "merica",
        "jamur": "jamur",
        "cheese": "keju",
        "vegetable": "sayur"
    }
    
    for i, item in enumerate(items):
        # Ubah _id menjadi string
        item['_id'] = str(item['_id'])
        if 'category' in item:
            item['category'] = str(item['category'])
        result[i] = item
        
        if 'spiceLevel' in item:
          spiceLevel = item['spiceLevel']
        else:
          spiceLevel = 1

        ingredient_values = {column: 0 for column in ingredients_dict.values()}  # Set all values to zero initially
        
        if 'ingredient' in item:
            ingredients = item['ingredient']
            
            if isinstance(ingredients, str):  # Handle case when ingredient is a string
                ingredients = [ingredients.lower()]
            elif isinstance(ingredients, list):  # Handle case when ingredient is an array
                ingredients = [ingredient.lower() for ingredient in ingredients]

            # Periksa setiap bahan dan atur nilai kolom yang sesuai
            ingredient_values = {column: 1 if ingredient in ingredients else 0 for ingredient, column in ingredients_dict.items()}

        # Tambahkan data ke list
        data.append({
        "Spice Level": spiceLevel,
        **ingredient_values
    })

    # Tutup koneksi MongoDB
    client.close()
    
    # Buat dataframe dari list
    df = pd.DataFrame(data)
    return result, df

def parse_user_data(user):
  data = []
      
  # Daftar bahan dan kolom yang sesuai
  likeIngredients_dict = {
      "pepperoni": "pepperoni",
      "daging": "daging",
      "ayam": "ayam",
      "tomat": "tomat",
      "paprika": "paprika",
      "saus": "saus",
      "merica": "merica",
      "jamur": "jamur",
      "cheese": "keju"
  }
      
  if 'spiceLevel' in user:
    spiceLevel = user['spiceLevel']
  else:
    spiceLevel = 1

  likeIngredient_values = {column: 0 for column in likeIngredients_dict.values()}  # Set all values to zero initially
  
  if 'likeIngredient' in user:
      likeIngredients = user['likeIngredient']
      
      if isinstance(likeIngredients, str):  # Handle case when ingredient is a string
          likeIngredients = [likeIngredients.lower()]
      elif isinstance(likeIngredients, list):  # Handle case when ingredient is an array
          likeIngredients = [ingredient.lower() for ingredient in likeIngredients]

      # Periksa setiap bahan dan atur nilai kolom yang sesuai
      likeIngredient_values = {column: 1 if likeIngredient in likeIngredients else 0 for likeIngredient, column in likeIngredients_dict.items()}


  # Tambahkan data ke list
  data.append({
  "Spice Level": spiceLevel,
  **likeIngredient_values
  
  })
  return pd.DataFrame(data)

def encode_spice_level(menu_df, user_df):
  scaler = MinMaxScaler(feature_range=(0, 1))
  menu_df['Spice Level'] = scaler.fit_transform(menu_df[['Spice Level']])
  user_df['Spice Level'] = scaler.transform(user_df[['Spice Level']])

def preprocess(menu_df, user_df):
  num_foods = menu_df.shape[0]
  encode_spice_level(menu_df, user_df)
#   menu_ids = menu_df[['food_id', 'Name of Pizza']]
  # menu_ids.set_index('food_id', inplace=True)
  menu_vecs = menu_df[['Spice Level','pepperoni','daging','ayam','tomat','paprika','saus','merica','jamur','keju','sayur']].to_numpy(dtype=float)
  user_vecs = np.tile(user_df.to_numpy(dtype=float), (num_foods, 1))
  return menu_vecs, user_vecs

def get_recommended_foods(predictions, menu):
    sorted_index = np.argsort(-predictions,axis=0).reshape(-1).tolist()
    top_index = sorted_index[:4]
    return [menu[idx] for idx in top_index]
    # sorted_predictions   = predictions[sorted_index]
    # sorted_items = menu_ids.iloc[sorted_index, :].reset_index(drop=True)
    # #   print(sorted_items.food_id[0])
    # top_4 = sorted_items.loc[:3, 'food_id'].tolist()

    # return [str(object_id) for object_id in top_4]