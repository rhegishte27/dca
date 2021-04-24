import BaseEnum from './BaseEnum';

export default class BackupKeepInterval extends BaseEnum {

    public static DAYS = new BackupKeepInterval('1', 'project:backupsEnum:days');
    public static WEEKS = new BackupKeepInterval('2', 'project:backupsEnum:weeks');
    public static MONTHS = new BackupKeepInterval('3', 'project:backupsEnum:months');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}


