package main

import (
	"database/sql"
	"encoding/json"
	"flag"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jinzhu/now"
	"math"
	"net/http"
	"strconv"
	"time"
)

var port int = 1234 //can also be provided via command line flags
var tableName string = "data"
var sqlString string = "co2:password@/co2" //can also be provided via command line flags

func main() {

	sqlFlag := flag.String("sql", sqlString, "MySQL credidential in Go database format")
	portFlag := flag.Int("p", port, "Port to listen to")
	flag.Parse()

	sqlString = *sqlFlag
	port = *portFlag
	http.HandleFunc("/year", yearHandler)
	http.ListenAndServe(":"+strconv.Itoa(port), nil)
}

func yearHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Access-Control-Allow-Origin", "*")

	current := time.Now()
	day := now.BeginningOfDay().Sub(now.EndOfDay())
	var rows []Row

	for i := 0; i < 365; i++ {
		current = current.Add(-day)
		where := "time > '" + now.New(current).BeginningOfDay().Format(time.RFC822) + "' and time < '" + now.New(current).EndOfDay().Format(time.RFC822) + "'"
		gas, _ := average(where)
		rows = append([]Row{Row{VStructs: []VStruct{VStruct{Value: i}, VStruct{Value: gas}}}}, rows...)
	}
	result := getGraphJson(rows, []string{"asdf", "asdf2"})
	fmt.Fprint(w, result)
}

func getGraphJson(rows []Row, labels []string) string {
	result := &GraphResponse{}
	result.Cols = append(result.Cols, Col{Type: "number", Label: labels[0]})
	result.Cols = append(result.Cols, Col{Type: "number", Label: labels[1]})
	result.Rows = append(result.Rows, rows...)
	responseStr, err := json.Marshal(result)
	check(err)
	return string(responseStr)
}

func numberOf(where string) int {
	var query string
	if where == "" {
		query = "select * from " + tableName + ";"
	} else {
		query = "select * from " + tableName + " where " + where + ";"
	}

	con, err := sql.Open("mysql", sqlString)
	check(err)
	defer con.Close()

	rows, err := con.Query(query)
	check(err)
	var i int = 0

	for rows.Next() {
		i++
	}
	return i
}

func average(where string) (gas, temperature float64) {
	var query string
	if where == "" {
		query = "select gas, temperature from " + tableName + ";"
	} else {
		query = "select gas, temperature from " + tableName + " where " + where + ";"
	}

	con, err := sql.Open("mysql", sqlString)
	check(err)
	defer con.Close()

	rows, err := con.Query(query)
	check(err)

	var sumGas float64 = 0
	var sumTemp float64 = 0
	var i int = 0

	for rows.Next() {
		i++
		var gas, temp float64
		err := rows.Scan(&gas, &temp)
		check(err)
		sumGas += gas
		sumTemp += temp
	}

	if i == 0 {
		return 0, 0
	}

	return float64(sumGas) / float64(i), float64(sumTemp) / float64(i)
}

func Round(f float64) float64 {
	return math.Floor(f + .5)
}

func RoundOn(f float64, places int) float64 {
	shift := math.Pow(10, float64(places))
	return Round(f*shift) / shift
}

func check(err error) {
	if err != nil {
		panic(err)
	}
}

type GraphResponse struct {
	Cols []Col "json:\"cols\""
	Rows []Row "json:\"rows\""
}

type Col struct {
	Id    string "json:\"id\""
	Label string "json:\"label\""
	Type  string "json:\"type\""
}

type Row struct {
	VStructs []VStruct "json:\"c\""
}

type VStruct struct {
	Value interface{} "json:\"v\""
}
