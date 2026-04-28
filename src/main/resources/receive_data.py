import requests
import pandas as pd
from datetime import datetime, timedelta

# --- تنظیم بازهٔ زمانی: یک سال اخیر ---
end_date = datetime.utcnow()
start_date = end_date - timedelta(days=365)

# تبدیل تاریخ‌ها به میلی‌ثانیه برای API
start_ms = int(start_date.timestamp() * 1000)
end_ms = int(end_date.timestamp() * 1000)

# --- URL API بایننس برای داده‌های روزانه ---
url = "https://api.binance.com/api/v3/klines"

params = {
    "symbol": "BTCUSDT",
    "interval": "1d",
    "startTime": start_ms,
    "endTime": end_ms,
    "limit": 1000        # حداکثر
}

response = requests.get(url, params=params)
response.raise_for_status()

data = response.json()

# ساخت DataFrame
df = pd.DataFrame(data, columns=[
    "open_time", "open", "high", "low", "close", "volume",
    "close_time", "quote_asset_volume", "number_of_trades",
    "taker_buy_base", "taker_buy_quote", "ignore"
])

# انتخاب ستون‌های لازم
df = df[["open_time", "open", "high", "low", "close", "volume"]]

# تبدیل timestamp
df["date"] = pd.to_datetime(df["open_time"], unit='ms')

# حذف ستون timestamp اصلی
df = df[["date", "open", "high", "low", "close", "volume"]]

# ذخیره در اکسل
output_file = "btc_usdt_last_year.xlsx"
df.to_excel(output_file, index=False)

print("فایل با موفقیت ساخته شد:", output_file)
