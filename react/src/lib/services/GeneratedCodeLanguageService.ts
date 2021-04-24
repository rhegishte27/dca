import GeneratedCodeLanguage from '../domain/entities/GeneratedCodeLanguage';
import GeneratedCodeLanguageRepository from '../domain/repositories/GeneratedCodeLanguageRepository';

export default class GeneratedCodeLanguageService {
    constructor(private generatedCodeLanguageRepository: GeneratedCodeLanguageRepository) {
    }

    async findAll(): Promise<GeneratedCodeLanguage[]> {
        return this.generatedCodeLanguageRepository.findAll();
    }
}