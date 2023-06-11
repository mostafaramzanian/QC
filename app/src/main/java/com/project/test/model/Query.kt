package com.project.test.model

import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.widget.Toast

class Query(private val context: Activity) {
    private val db = Database(context).writableDatabase
    fun login(user: String): Cursor {
        val cursor =
            db.rawQuery("SELECT * FROM users WHERE username ='$user' AND is_deleted = 0", null)
        return (cursor)
    }

    fun userProcesses(userId: Int): Cursor {
        val cursor = db.rawQuery("SELECT * FROM user_processes WHERE user_id ='$userId'", null)
        return (cursor)
    }

    fun userTypes(user_types: String): Cursor {
        val cursor = db.rawQuery("SELECT * FROM user_types WHERE name ='$user_types'", null)
        return (cursor)
    }

    fun controlStation(ProcessesId: Int): Cursor {
        val cursor = db.rawQuery(
            "SELECT * FROM control_station WHERE process_id ='$ProcessesId' AND is_deleted = 0 ORDER BY order_id ASC",
            null
        )
        return (cursor)
    }

    fun cp(controlStationId: String): Cursor {
        val cursor = db.rawQuery(
            "SELECT * FROM cp WHERE control_station_id ='$controlStationId' AND is_active = 1 ",
            null
        )
        return (cursor)
    }

    fun selectFromCp(cp_code: String): Cursor {
        val cursor = db.rawQuery(
            "SELECT * FROM cp WHERE cp_code ='$cp_code' AND is_active = 1 ORDER BY cp_version DESC",
            null
        )
        return (cursor)
    }

    fun cpStandardParameters(cp_Id: Int): Cursor {
        val cursor = db.rawQuery("SELECT * FROM cp_standard_parameters WHERE cp_id ='$cp_Id'", null)
        return (cursor)
    }

    fun cpStandardParametersSelected(parameter_id: Int): Cursor {
        val cursor =
            db.rawQuery("SELECT * FROM cp_standard_parameters WHERE id ='$parameter_id'", null)
        return (cursor)
    }

    fun insertCpReports(values: ContentValues): Long {
        return db.insert("cp_reports", null, values)
    }

    fun insertCpReportsParameters(values: ContentValues): Long {
        return db.insert("cp_reports_parameters", null, values)
    }

    fun cpSelectReports(id: Int): Cursor {
        val cursor =
            db.rawQuery("SELECT * FROM cp_reports WHERE cp_id ='$id' AND is_draft = '1'", null)
        return (cursor)
    }

    fun cpSelectReports1(id: Int,isDraft: Int): Cursor {
        val cursor =
            db.rawQuery("SELECT * FROM cp_reports WHERE cp_id ='$id' AND is_draft = '$isDraft'", null)
        return (cursor)
    }

    fun updateCpReports(id: Int, values: ContentValues): Int {
        val selection = "cp_id = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.update("cp_reports", values, selection, selectionArgs)
        return (cursor)
    }

    fun insertCpReportsInfo(values: ContentValues): Long {
        return db.insert("cp_reports_info", null, values)
    }

    fun cpReportsParameters(id: Int): Cursor {
        val cursor = db.rawQuery(
            "SELECT * FROM cp_reports_parameters WHERE report_id ='$id' ORDER BY report_order ASC",
            null
        )
        return (cursor)
    }

    fun cpReportsSelectedParameters(id: Int): Cursor {
        val cursor = db.rawQuery("SELECT * FROM cp_reports_parameters WHERE report_id ='$id'", null)
        return (cursor)
    }

    fun reportActive(): Cursor {
        val cursor = db.rawQuery(
            "SELECT cp_reports_parameters.*, cp_reports.*,control_station.*,cp.*,cp_reports.created_datetime AS time FROM cp_reports_parameters JOIN cp_reports ON cp_reports_parameters.report_id = cp_reports.id JOIN cp ON cp_reports.cp_id = cp.id JOIN control_station ON cp.control_station_id = control_station.id WHERE is_draft = 1 ORDER BY id DESC LIMIT 1",
            null
        )
        return (cursor)
    }

    fun reportNotActive(): Cursor {
        val cursor = db.rawQuery(
            "SELECT cp_reports_parameters.*, cp_reports.*,control_station.*,cp.*,cp_reports.created_datetime AS time FROM cp_reports_parameters JOIN cp_reports ON cp_reports_parameters.report_id = cp_reports.id JOIN cp ON cp_reports.cp_id = cp.id JOIN control_station ON cp.control_station_id = control_station.id WHERE is_draft = 0 ORDER BY id DESC LIMIT 1",
            null
        )
        return (cursor)
    }

    fun reportNotActive1(): Cursor {
        val cursor = db.rawQuery(
            "SELECT cp.*, cp_reports.*, control_station.*,cp_reports.created_datetime AS firstTime,cp_reports.id AS cp_report FROM cp JOIN cp_reports ON cp.id = cp_reports.cp_id JOIN control_station ON cp_reports.station_id = control_station.id WHERE is_draft='0'",
            null
        )
        return (cursor)
    }

    fun finalRegister(cpId:Int): Cursor {
        val cursor = db.rawQuery(
            "SELECT * FROM cp_reports LEFT JOIN cp_reports_info ON cp_reports.id =  cp_reports_info.cp_report_id  WHERE cp_id ='$cpId' AND is_draft='0'",
            null
        )
        return (cursor)
    }

    fun tools(toolTypeId: Int, processId: Int): Cursor {
        val cursor = db.rawQuery(
            "SELECT * FROM tools WHERE tool_type_id ='$toolTypeId' AND process_id = '$processId' AND is_deleted = '0'",
            null
        )
        return (cursor)
    }

    fun documents(cpId: Int): Cursor {
        val cursor = db.rawQuery(
            "SELECT * FROM cp_instructions JOIN manufacturing_instructions ON cp_instructions.instruction_id = manufacturing_instructions.id JOIN instructions_doc_types ON manufacturing_instructions.document_type = instructions_doc_types.id WHERE cp_id = '$cpId' AND is_deleted='0'",
            null
        )
        return (cursor)
    }

    fun otherReportQuery(cpId: Int): Cursor {
        val cursor = db.rawQuery(
            "SELECT cp.*, cp_reports.*, control_station.*,cp_reports.created_datetime AS firstTime,cp_reports.id AS cp_report FROM cp JOIN cp_reports ON cp.id = cp_reports.cp_id JOIN control_station ON cp_reports.station_id = control_station.id WHERE cp_id <> '$cpId' AND is_draft='1'",
            null
        )
        return (cursor)
    }

    fun otherReportQuery1(cpId: Int): Cursor {
        val cursor = db.rawQuery(
            "SELECT cp.*, cp_reports.*, control_station.*,cp_reports.created_datetime AS firstTime,cp_reports.id AS cp_report FROM cp JOIN cp_reports ON cp.id = cp_reports.cp_id JOIN control_station ON cp_reports.station_id = control_station.id WHERE is_draft='1'",
            null
        )
        return (cursor)
    }

    fun cpReportsJoin(reportId: Int): Cursor {
        val cursor = db.rawQuery(
            "SELECT cp_reports_parameters.*, cp_reports.*,cp_reports_parameters.created_datetime AS lastTime FROM cp_reports JOIN cp_reports_parameters ON cp_reports.id = cp_reports_parameters.report_id WHERE report_id = '$reportId' ORDER BY report_order DESC LIMIT 1",
            null
        )
        return (cursor)
    }

    fun toolsSelected(nameTool: String): Cursor {
        val cursor = db.rawQuery("SELECT * FROM tools WHERE title ='$nameTool'", null)
        return (cursor)
    }


    fun count(table_name: String): Cursor {
        val cursor = db.rawQuery("SELECT COUNT(*) FROM '$table_name'", null)
        return (cursor)
    }

    fun count2(isDraft:Int): Cursor {
        val cursor = db.rawQuery("SELECT COUNT(*) FROM cp_reports WHERE is_draft ='$isDraft' ", null)
        return (cursor)
    }

    fun count1(tableName: String, reportId: Int): Cursor {
        val cursor =
            db.rawQuery("SELECT COUNT(*) FROM '$tableName' WHERE report_id = '$reportId'", null)
        return (cursor)
    }


    fun getLastRecord(): Cursor {
        val cursor = db.rawQuery("SELECT * FROM cp_reports ORDER BY id DESC LIMIT 1", null)
        return (cursor)
    }


    fun delete(tableName: String, columnName: String, id1: Int) {
        val selectionArgs = arrayOf(id1.toString())
        val rowsDeleted = db.delete(tableName, "$columnName = ?", selectionArgs)
        if (rowsDeleted > 0) {
            Toast.makeText(context, "Row deleted successfully", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "No rows deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteAll(tableName: String) {
        db.delete(tableName, null, null)
        db.close()
    }
}


/*

        if (cursor.moveToFirst()) {
            val storedPasswordHash = cursor.getString(cursor.getColumnIndexOrThrow("passwd"))
            /*
            if (BCrypt.checkpw(password, storedPasswordHash)) {
                Toast.makeText(this, "Password is correct", Toast.LENGTH_SHORT).show()
            }

             */
        }else {
            Toast.makeText(applicationContext, "Password is incorrect", Toast.LENGTH_SHORT).show()
        }

 */