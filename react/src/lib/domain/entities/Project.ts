import BackupInterval from './BackupInterval';
import BackupKeepInterval from './BackupKeepInterval';
import BaseEntity from './BaseEntity';
import Compiler from './Compiler';
import GeneratedCodeLanguage from './GeneratedCodeLanguage';
import ProjectSyncSetting from './ProjectSyncSetting';
import ProjectSystem from './ProjectSystem';

export default class Project extends BaseEntity {
    public id: string = '';
    public identifier: string = '';
    public description: string = '';
    public generatedCodeLanguage: GeneratedCodeLanguage = new GeneratedCodeLanguage();
    public compiler: Compiler = new Compiler();
    public isSynchronizationEnabled: boolean = false;
    public isBackupEnabled: boolean = false;
    public backupInterval: BackupInterval = new BackupInterval();
    public numberOfBackupIntervals: string | undefined;
    public backupKeepInterval: BackupKeepInterval  = new BackupKeepInterval();
    public numberOfBackupKeepIntervals: string | undefined;
    public isSynchronizeProjectElementsEnabled: boolean = false;
    public projectSyncSettings: ProjectSyncSetting[] = [];
    public systems: ProjectSystem[] = [];
    public lastBackup?: string = '';
}
