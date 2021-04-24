import GeneratedCodeLanguage from '../../domain/entities/GeneratedCodeLanguage';
import GeneratedCodeLanguageRepository from '../../domain/repositories/GeneratedCodeLanguageRepository';
import {ApiGateway} from '../config/apiGateway';

export default class GeneratedCodeLanguageApiRepository implements GeneratedCodeLanguageRepository {
    readonly URL: string = '/generatedcodelanguages';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<GeneratedCodeLanguage[]> {
        return this.api.getArray(this.URL, {outType: GeneratedCodeLanguage});
    }
}