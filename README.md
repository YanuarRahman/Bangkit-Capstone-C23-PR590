# Bangkit-Capstone-C23-PR590

# FaceRecognition
Backend untuk identifikasi dan autentikasi seseorang dengan menggunakan fitur wajah yang dimiliki

Inbound port: 8000 (FastAPI)

Route yang tersedia:

'/recognition' (POST): identifikasi wajah

# Aktivasi
## Menggunakan Conda
```
conda activate base
uvicorn main:app
```
#Mengakses API
- API akan tersedia di http://localhost:8000 . Request bisa dikirimkan ke endpoint /recognition

Contoh body request:
```{
    "class": "messi",
    "confidence": 0.9315411448478699
}
```

