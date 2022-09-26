from http.server import HTTPServer, BaseHTTPRequestHandler

from io import BytesIO

class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):
    currentNum = 1

    def do_GET(self):
        self.send_response(200)
        self.end_headers()
        self.wfile.write(b'Hello, world!')

    def do_POST(self):
        content_length = int(self.headers['Content-Length'])
        body = self.rfile.read(content_length)
        strData = body.decode('UTF-8')

        if(strData == "status=finish"):
            SimpleHTTPRequestHandler.currentNum = 1
            self.send_response(200)
            self.end_headers()
        else:

            strData = strData.replace('%20', ' ')
            strData = strData.replace('%2B', '+')
            strData = strData.replace('%3A', ':')
            strData = strData.replace('&', ' ')

            if(strData[strData.find('type=') + 5] == '1'):
                strData = strData[:strData.find('type=') + 5] + 'INCOMING ' +  strData[strData.find('type=') + 6:]
            elif (strData[strData.find('type=') + 5] == '2'):
                strData = strData[:strData.find('type=') + 5] + 'OUTGOING ' +  strData[strData.find('type=') + 6:]
            elif (strData[strData.find('type=') + 5] == '3'):
                strData = strData[:strData.find('type=') + 5] + 'MISSED ' +  strData[strData.find('type=') + 6:]
            SimpleHTTPRequestHandler.printData(strData)
            self.send_response(200)
            self.end_headers()

    def printData(strData):

        if (SimpleHTTPRequestHandler.currentNum == 1):
            f = open('journal.txt', 'w')
        else:
            f = open('journal.txt', 'a')

        f.write(str(SimpleHTTPRequestHandler.currentNum) + ') ' + strData + '\n')
        f.close()
        SimpleHTTPRequestHandler.currentNum +=1


httpd = HTTPServer(('localhost', 8080), SimpleHTTPRequestHandler)
httpd.serve_forever()