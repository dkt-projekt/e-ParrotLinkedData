package de.dkt.eservices.eparrotrepository.ddbb;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class NLPModelMapper implements RowMapper<NLPModel> {

	public NLPModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		NLPModel m = new NLPModel();
		m.setModelId(rs.getInt("modelId"));
		m.setModelName(rs.getString("modelName"));

		String type = rs.getString("modelType");
		m.setType(type);

		m.setInformat(rs.getString("informat"));
		m.setOutformat(rs.getString("outformat"));
		m.setUrl(rs.getString("url"));
		if(type.equalsIgnoreCase("ner")){
			m.setAnalysis(rs.getString("analysis"));
			m.setLanguage(rs.getString("language"));
			m.setModels(rs.getString("models"));
			m.setMode(rs.getString("mode"));
		}
		else if(type.equalsIgnoreCase("timex")){
			m.setAnalysis(rs.getString("analysis"));
			m.setLanguage(rs.getString("language"));
			m.setModels(rs.getString("models"));
			m.setMode(rs.getString("mode"));
		}
		else if(type.equalsIgnoreCase("dict")){
			m.setAnalysis(rs.getString("analysis"));
			m.setLanguage(rs.getString("language"));
			m.setModels(rs.getString("models"));
			m.setMode(rs.getString("mode"));
		}
		else if(type.equalsIgnoreCase("translate")){
			m.setAnalysis(rs.getString("analysis"));
			m.setLanguage(rs.getString("language"));
			m.setModels(rs.getString("models"));
			m.setMode(rs.getString("mode"));
		}
		else if(type.equalsIgnoreCase("coref") || type.equalsIgnoreCase("relExtract")){
			m.setAnalysis(rs.getString("analysis"));
			m.setLanguage(rs.getString("language"));
			m.setModels(rs.getString("models"));
			m.setMode(rs.getString("mode"));
		}
		return m;
	}

}
