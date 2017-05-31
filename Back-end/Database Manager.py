import pyodbc

if __name__ == '__main__':
    cnxn = pyodbc.connect('DRIVER={PostgreSQL Unicode};'
                          'DATABASE=g1627137_u;'
                          'UID=g1627137_u;'
                          'PWD=Vk426n3Kjx;'
                          'SERVER=db.doc.ic.ac.uk;'
                          'PORT=5432;'
                          )
    print "here"
    con = pyodbc.connect(cnxn)
    cursor = con.execute("\list")
    print "here"
    print cursor.fetchone()
    cursor.close()
    con.close()
