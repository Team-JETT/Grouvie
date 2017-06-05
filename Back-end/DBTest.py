from DBManager import DBManager

MAKE_TEST_TABLE = """
CREATE TABLE TEST (
    NUMBER          INT,
    CHARACTER       CHAR(1),
    STRING          TEXT
    )
"""

DROP_TEST_TABLE = """
DROP TABLE TEST
"""

INSERT_INTO_TEST_TABLE = """
INSERT INTO TEST
VALUES
(%s, %s, %s)
"""

SELECT_ALL_IN_TABLE = """
SELECT * FROM TEST
"""

class DBTest:

    dbManager = None
    def __init__(self):
        global dbManager
        dbManager = DBManager()
        self.testMakingOfTestTable()
        self.testInsertingIntoTestTable()
        self.testReadingFromTestTableAndVerifyData()


    def testMakingOfTestTable(self):
        global dbManager, MAKE_TEST_TABLE
        cursor = dbManager.establish_connection()
        cursor.execute(MAKE_TEST_TABLE)
        dbManager.close_connection()

    def testInsertingIntoTestTable(self):
        global dbManager, INSERT_INTO_TEST_TABLE
        cursor = dbManager.establish_connection()
        cursor.execute(INSERT_INTO_TEST_TABLE)
        dbManager.close_connection()

    def testReadingFromTestTableAndVerifyData(self):
        global dbManager, SELECT_ALL_IN_TABLE
        cursor = dbManager.establish_connection()
        cursor.execute(SELECT_ALL_IN_TABLE)
        result = dbManager.cursor.fetchall()
        dbManager.close_connection()
        print result

if __name__ == '__main__':
    dbTest = DBTest()

