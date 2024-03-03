import yfinance as yf

def get_current_price(symbol):
    ticker = yf.Ticker(symbol)
    today_data = ticker.history(period='1d')
    return today_data['Close'][0]