import BaseEnum from './BaseEnum';

export default class BackupInterval extends BaseEnum {

    public static DAYS = new BackupInterval('1', 'project:backupsEnum:days');
    public static WEEKS = new BackupInterval('2', 'project:backupsEnum:weeks');
    public static MONTHS = new BackupInterval('3', 'project:backupsEnum:months');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}