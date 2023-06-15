import numpy as np
import pandas as pd
import os
from pymongo import MongoClient
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
from bson.objectid import ObjectId
from dotenv import load_dotenv

def get_menu_data(conn, database_name, collection_name):
    # Koneksi ke MongoDB
    client = MongoClient(conn)
    db = client[database_name]
    collection = db[collection_name]

    # Ambil semua item food dari database
    items = collection.find()

    # List untuk menyimpan data
    data = []

    # Loop melalui setiap item food
    for item in items:
        # Ambil nilai field-field yang diperlukan
        food_id = item["_id"]
        name = item["name"]
        spice_level = item["spiceLevel"]
        image_url = item["imageUrl"]
        ingredients = item["ingredients"]

        # Inisialisasi nilai kolom bahan
        pepperoni = 0
        beef = 0
        chicken = 0
        tomatoes = 0
        capsicum = 0
        sauce = 0
        peppers = 0
        mushroom = 0
        cheese = 0
        vegetable = 0

        # Periksa setiap bahan dan atur nilai kolom bahan yang sesuai
        if "pepperoni" in ingredients:
            pepperoni = 1
        if "beef" in ingredients:
            beef = 1
        if "chicken" in ingredients:
            chicken = 1
        if "tomatoes" in ingredients:
            tomatoes = 1
        if "capsicum" in ingredients:
            capsicum = 1
        if "sauce" in ingredients:
            sauce = 1
        if "peppers" in ingredients:
            peppers = 1
        if "mushroom" in ingredients:
            mushroom = 1
        if "cheese" in ingredients:
            cheese = 1
        if "vegetable" in ingredients:
            vegetable = 1

        # Tambahkan data ke list
        data.append({
            "food_id": food_id,
            "Name of Pizza": name,
            "Spice Level": spice_level,
            "Pepperoni": pepperoni,
            "Beef": beef,
            "Chicken": chicken,
            "Tomatoes": tomatoes,
            "Capsicum": capsicum,
            "Sauce": sauce,
            "Peppers": peppers,
            "Mushroom": mushroom,
            "Cheese": cheese,
            "Vegetable": vegetable,
            "Image_url": image_url
        })

    # Tutup koneksi MongoDB
    client.close()

    # Buat dataframe dari list
    df = pd.DataFrame(data)
    return df


def parse_user_data(user):
    data = []

    user_id = user['_id']
    name = user["name"]
    spice_level = user["spiceLevel"]
    # image_url = item["imageUrl"]
    ingredients = user["likedIngredients"]

    # Inisialisasi nilai kolom bahan
    pepperoni = 0
    beef = 0
    chicken = 0
    tomatoes = 0
    capsicum = 0
    sauce = 0
    peppers = 0
    mushroom = 0
    cheese = 0

    # Periksa setiap bahan dan atur nilai kolom bahan yang sesuai
    if "pepperoni" in ingredients:
        pepperoni = 1
    if "beef" in ingredients:
        beef = 1
    if "chicken" in ingredients:
        chicken = 1
    if "tomatoes" in ingredients:
        tomatoes = 1
    if "capsicum" in ingredients:
        capsicum = 1
    if "sauce" in ingredients:
        sauce = 1
    if "peppers" in ingredients:
        peppers = 1
    if "mushroom" in ingredients:
        mushroom = 1
    if "cheese" in ingredients:
        cheese = 1

    # Tambahkan data ke dataframe
    data.append({
                "Spice Level": spice_level,
                "Pepperoni": pepperoni,
                "Beef": beef,
                "Chicken": chicken,
                "Tomatoes": tomatoes,
                "Capsicum": capsicum,
                "Sauce": sauce,
                "Peppers": peppers,
                "Mushroom": mushroom,
                "Cheese": cheese
            })
    df = pd.DataFrame(data)
    return user_id, df

def encode_spice_level(menu_df, user_df):
  scaler = MinMaxScaler(feature_range=(0, 1))
  menu_df['Spice Level'] = scaler.fit_transform(menu_df[['Spice Level']])
  user_df['Spice Level'] = scaler.transform(user_df[['Spice Level']])

def preprocess(menu_df, user_df):
  num_foods = menu_df.shape[0]
  encode_spice_level(menu_df, user_df)
  menu_ids = menu_df[['food_id', 'Name of Pizza']]
  # menu_ids.set_index('food_id', inplace=True)
  menu_vecs = menu_df[['Spice Level','Pepperoni','Beef','Chicken','Tomatoes','Capsicum','Sauce','Peppers','Mushroom','Cheese','Vegetable']].to_numpy(dtype=float)
  user_vecs = np.tile(user_df.to_numpy(dtype=float), (num_foods, 1))
  return menu_ids, menu_vecs, user_vecs

def get_recommended_foods(predictions, menu_ids):
    sorted_index = np.argsort(-predictions,axis=0).reshape(-1).tolist()
    # sorted_predictions   = predictions[sorted_index]
    sorted_items = menu_ids.iloc[sorted_index, :].reset_index(drop=True)
    #   print(sorted_items.food_id[0])
    top_6 = sorted_items.loc[:5, 'food_id'].tolist()
    return [str(object_id) for object_id in top_6]