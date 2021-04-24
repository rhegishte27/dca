import BaseEnum from './BaseEnum';

export default class Feature extends BaseEnum {
    public static SYSTEM_SETTINGS = new Feature('1', 'user:featuresEnum:systemSettings');
    public static ORGANIZATIONS_AND_USERS = new Feature('2', 'user:featuresEnum:organizationsUsers');
    public static PROJECT_MANAGEMENT = new Feature('3', 'user:featuresEnum:projectManagement');
    public static PROJECT_DATA_MAPS = new Feature('4', 'user:featuresEnum:projectDatamaps');
    public static PROJECT_TABLES = new Feature('5', 'user:featuresEnum:projectTables');
    public static SYSTEM_MANAGEMENT = new Feature('6', 'user:featuresEnum:systemManagement');
    public static SYSTEM_CODE_ANALYSIS = new Feature('7', 'user:featuresEnum:systemCodeAnalysis');
    public static SYSTEM_DATA_OBJECTS = new Feature('8', 'user:featuresEnum:systemDataObjects');
    public static SYSTEM_TRANSACTIONS = new Feature('9', 'user:featuresEnum:systemTransaction');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}
