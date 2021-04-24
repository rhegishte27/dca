import Language from '../entities/Language';

export default interface LanguageRepository {
    findAll(): Promise<Language[]>;
}