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

TEST_RESULT = [(0, 'c', 'text')]


class DBTest:

    def __init__(self):
        dbManager = DBManager()
        self.testMakingOfTestTable(dbManager)
        self.testInsertingIntoTestTable(dbManager)
        print self.testReadingFromTestTableAndVerifyData(dbManager)
        self.dropTestTable(dbManager)

    def testMakingOfTestTable(self, dbManager):
        global MAKE_TEST_TABLE
        cnxn, cursor = dbManager.establish_connection()
        cursor.execute(MAKE_TEST_TABLE)
        dbManager.close_connection(cnxn, cursor)

    def testInsertingIntoTestTable(self, dbManager):
        global INSERT_INTO_TEST_TABLE
        cnxn, cursor = dbManager.establish_connection()
        cursor.execute(INSERT_INTO_TEST_TABLE, ("0", "c", "text"))
        dbManager.close_connection(cnxn, cursor)

    def testReadingFromTestTableAndVerifyData(self, dbManager):
        global SELECT_ALL_IN_TABLE, TEST_RESULT
        cnxn, cursor = dbManager.establish_connection()
        cursor.execute(SELECT_ALL_IN_TABLE)
        result = cursor.fetchall()
        dbManager.close_connection(cnxn, cursor)
        return "Success" if result == TEST_RESULT else "Failure"

    def dropTestTable(self, dbManager):
        global DROP_TEST_TABLE
        cnxn, cursor = dbManager.establish_connection()
        cursor.execute(DROP_TEST_TABLE)
        dbManager.close_connection(cnxn, cursor)

if __name__ == '__main__':
    dbTest = DBTest()
