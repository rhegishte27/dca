import Language from '../../domain/entities/Language';
import LanguageRepository from '../../domain/repositories/LanguageRepository';
import {ApiGateway} from '../config/apiGateway';

export default class LanguageApiRepository implements LanguageRepository {
    readonly URL: string = '/languages';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<Language[]> {
        return this.api.getArray(this.URL, {outType: Language});
    }
}