import GeneratedCodeLanguage from '../entities/GeneratedCodeLanguage';

export default interface GeneratedCodeLanguageRepository {
    findAll(): Promise<GeneratedCodeLanguage[]>;
}