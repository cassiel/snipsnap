java -cp lib/mckoidb.jar com.mckoi.tools.JDBCQueryTool \
       -url "jdbc:mckoi:local://./applications/$3/db.conf" \
       -u "$1" -p "$2"
