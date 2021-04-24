import BackupInterval from '../entities/BackupInterval';

export default interface BackupIntervalRepository {
    findAll(): Promise<BackupInterval[]>;
}