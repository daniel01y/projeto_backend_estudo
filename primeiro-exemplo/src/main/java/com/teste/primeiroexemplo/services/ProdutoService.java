package com.teste.primeiroexemplo.services;

import com.teste.primeiroexemplo.model.Produto;
import com.teste.primeiroexemplo.model.exception.ResourceNotFoundException;
import com.teste.primeiroexemplo.repository.ProdutoRepository;
import com.teste.primeiroexemplo.repository.ProdutoRepositoryOld;
import com.teste.primeiroexemplo.shared.ProdutoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Método para retornar uma lista de produtos
     * @return lista de produtos
     * */
    public List<ProdutoDTO> obterTodos() {

        //Retorna um list de produtos model
        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Método que retorna o produto encontrado pelo seu id.
     * @param id do produto que será localizado.
     * @return Retorna um produto caso seja encontrado.
     * */
    public Optional<ProdutoDTO> obterPorId(Integer id) {
        //Obtendo optional de produto pelo id
        Optional<Produto> produto = produtoRepository.findById(id);

        //Se não encontrar lança exception
        if(produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com ID: " + id + " não encontrado");
        }

        //Convertendo o meu optional de produto em um produtoDTO
        ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);
        //Criando e retornando um optional de produtoDTO
        return Optional.of(dto);
    }

    /**
     * Metodo para adicionar produto na lista.
     * @param produtoDto será adicionado.
     * @return retorna o produto que será adicionado na lista.
     * */
    public ProdutoDTO adicionar(ProdutoDTO produtoDto) {
        //Removendo o id para conseguir fazer o cadastro
        produtoDto.setId(null);
        //Criando um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();
        //Converter o nosso produtoDTO em um produto model
        Produto produto = mapper.map(produtoDto, Produto.class);
        //Salvar o produto no banco
        produto = produtoRepository.save(produto);

        produtoDto.setId(produto.getId());
        //Retornar o produtoDTo atualizado
        return produtoDto;
    }

    /**
     * Metodo para deletar o produto por id.
     * @param id do produto a ser deletado.
     * */
    public void deletar(Integer id) {
        //Verificar se o produto existe
        Optional<Produto> produto = produtoRepository.findById(id);
        //Se não existir lança uma exception
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível deletar o produto com o id: " + id + " - Produto não existe");
        }
        //Deleta o produto pelo id
        produtoRepository.deleteById(id);
    }

    /**
     * Metodo para atualizar o produto na lista.
     * @param produtoDto que será atualizado.
     * @return Retorna o produto após atualizar a lista.
     * */
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDto) {
        //Passar o id para o produtoDto
        produtoDto.setId(id);
        //Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();
        //Converter o ProdutoDto em um produto
        Produto produto = mapper.map(produtoDto, Produto.class);
        //Atualizar o produto no banco de dados
        produtoRepository.save(produto);
        //Retornar o produtoDto atualizado
        return produtoDto;
    }
}
