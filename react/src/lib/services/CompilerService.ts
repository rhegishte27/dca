import Compiler from '../domain/entities/Compiler';
import CompilerRepository from '../domain/repositories/CompilerRepository';

export default class CompilerService {
    constructor(private compilerRepository: CompilerRepository) {
    }

    async findAll(): Promise<Compiler[]> {
        return this.compilerRepository.findAll();
    }
}