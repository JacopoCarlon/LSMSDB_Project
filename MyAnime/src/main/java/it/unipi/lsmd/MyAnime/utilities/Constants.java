package it.unipi.lsmd.MyAnime.utilities;

import java.io.File;

public final class Constants {
    // Folder names
    public static final String folderName_MainFolder = "data_MyAnimeLibrary";
    public static final String folderName_QueryResults = folderName_MainFolder + File.separator + "cachedQueryResults";
    //  public static final String folderName_AdminStats = folderName_MainFolder + File.separator + "adminStats";
    //  public static final String folderName_DatabaseUpdateScript = folderName_MainFolder + File.separator + "databaseUpdateScript";

    // File names for the statistics for the administrator
    public static final String fileName_AdminStats = "adminStats.json";

    // File names for the query results - Anime
    public static final String fileName_RankingAnimeByScoreAllTime = "rankingAnimeWithMinReviewsByAvgScoreAllTime.json";
    public static final String fileName_RankingAnimeByWatchersAllTime = "rankingAnimeByWatchersAllTime.json";


    // String for MongoDB connection
    //public static final String mongoDBConnectionUri = "mongodb://localhost:27017";
    public static final String mongoDBConnectionUri = "mongodb://localhost:27027,localhost:27028,localhost:27029/?replicaSet=MAL&w=1&readPreference=nearest&retryWrites=true";

    // Evita la creazione di istanze di questa classe
    private Constants() {
        throw new AssertionError("Classe Constants non istanziabile.");
    }
}
