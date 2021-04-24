import BackupKeepInterval from '../entities/BackupKeepInterval';

export default interface BackupKeepIntervalRepository {
    findAll(): Promise<BackupKeepInterval[]>;
}