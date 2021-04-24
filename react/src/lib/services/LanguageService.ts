import Language from '../domain/entities/Language';
import LanguageRepository from '../domain/repositories/LanguageRepository';

export default class LanguageService {
    constructor(private languageRepository: LanguageRepository) {
    }

    async findAll(): Promise<Language[]> {
        return this.languageRepository.findAll();
    }
}