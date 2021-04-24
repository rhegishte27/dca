import Configuration from '../entities/Configuration';

export default interface ConfigurationRepository {
    get(): Promise<Configuration>;
}