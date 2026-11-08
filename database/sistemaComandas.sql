-- Cria o banco de dados principal do sistema de comandas e encomendas com suporte a caracteres especiais (UTF-8)
CREATE DATABASE IF NOT EXISTS sistema_comandas  CHARACTER SET utf8mb4  COLLATE utf8mb4_unicode_ci;

USE sistema_comandas;

--------------------------------------------------------------------------------
-- 1. TABELA DE CLIENTES (Cadastro completo opcional para fidelidade/notas)
--------------------------------------------------------------------------------
CREATE TABLE cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,                -- Identificador único do cliente (Chave Primária)
    nome VARCHAR(150) NOT NULL,                              -- Nome completo do cliente
    email VARCHAR(150) UNIQUE NULL,                          -- E-mail do cliente (único no sistema, opcional)
    cpf VARCHAR(14) UNIQUE NULL,                             -- Número do CPF formatado (único no sistema, opcional)
    telefone VARCHAR(20) NULL,                               -- Telefone/WhatsApp de contato
    cep VARCHAR(10) NULL,                                    -- Código Postal (CEP) do endereço
    logradouro VARCHAR(150) NULL,                            -- Nome da rua, avenida, etc.
    numero VARCHAR(20) NULL,                                 -- Número do endereço
    complemento VARCHAR(100) NULL,                           -- Complemento (ex: Apto 101, Bloco B)
    unidade VARCHAR(50) NULL,                                -- Unidade ou bloco específico
    bairro VARCHAR(100) NULL,                                -- Bairro do endereço
    cidade VARCHAR(100) NULL,                                -- Cidade
    uf CHAR(2) NULL,                                         -- Unidade Federativa (Estado - ex: SP, RJ)
    dtCadastro DATETIME DEFAULT CURRENT_TIMESTAMP            -- Data e hora automática do cadastro do cliente
);

--------------------------------------------------------------------------------
-- 2. TABELA DE PRODUTOS (Catálogo base / Itens genéricos oferecidos)
--------------------------------------------------------------------------------
CREATE TABLE produtos (
    idProduto INT AUTO_INCREMENT PRIMARY KEY,                -- Identificador único do produto no catálogo
    nome VARCHAR(150) NOT NULL,                              -- Nome comercial do produto (ex: X-Burger, Cerveja)
    categoria VARCHAR(100) NULL,                             -- Categoria do produto (ex: Bebidas, Lanches, Porções)
    ingredientes TEXT NULL                                   -- Descrição dos ingredientes ou composição do produto
);

--------------------------------------------------------------------------------
-- 3. TABELA DE ESTOQUE (Controle de lotes físicos, validades e entradas/saídas)
--------------------------------------------------------------------------------
CREATE TABLE estoque (
    idEstoque INT AUTO_INCREMENT PRIMARY KEY,                -- Identificador único do lote/item no estoque
    idProduto INT NOT NULL,                                  -- Chave estrangeira ligando ao catálogo de produtos
    quantidade DECIMAL(10,3) NOT NULL DEFAULT 0.000,         -- Quantidade física disponível em estoque
    dtEntrada DATETIME DEFAULT CURRENT_TIMESTAMP,            -- Data e hora da entrada do lote no estoque
    dtSaida DATETIME NULL,                                   -- Data e hora da saída/baixa do lote
    motivoSaida VARCHAR(255) NULL,                           -- Motivo da baixa (ex: Venda, Avaria, Validade Vencida)
    validade DATE NULL,                                      -- Data de validade do lote do produto
    lote VARCHAR(50) NULL,                                   -- Número ou código do lote do fabricante
    peso DECIMAL(10,3) NULL,                                 -- Peso unitário ou do lote
    volume DECIMAL(10,3) NULL,                               -- Volume unitário ou do lote
    marca VARCHAR(100) NULL,                                 -- Marca do produto
    modelo VARCHAR(100) NULL,                                -- Modelo do produto (se aplicável)
    fabricante VARCHAR(100) NULL,                            -- Nome do fabricante
    fornecedor VARCHAR(150) NULL,                            -- Nome do fornecedor que entregou o lote
    notaEntrada VARCHAR(50) NULL,                            -- Número da nota fiscal de compra/entrada
    CONSTRAINT fk_estoque_produto FOREIGN KEY (idProduto) REFERENCES produtos(idProduto)
);

--------------------------------------------------------------------------------
-- 4. TABELA DE PREÇO DO ITEM EM ESTOQUE (Histórico ou vigência de custos e vendas)
--------------------------------------------------------------------------------
CREATE TABLE precoItemEstoque (
    idPrecoItemEstoque INT AUTO_INCREMENT PRIMARY KEY,       -- Identificador único do registro de preço
    idEstoque INT NOT NULL,                                  -- Referência ao lote/item físico no estoque
    valorCompra DECIMAL(10,2) NOT NULL,                      -- Preço de custo pago ao fornecedor
    valorVenda DECIMAL(10,2) NOT NULL,                       -- Preço praticado para venda ao consumidor
    dataPreco DATETIME DEFAULT CURRENT_TIMESTAMP,            -- Data e hora em que este preço entrou em vigor
    CONSTRAINT fk_preco_estoque FOREIGN KEY (idEstoque) REFERENCES estoque(idEstoque)
);

--------------------------------------------------------------------------------
-- 5. TABELA DE CARTÃO (Cartões físicos, comarcas ou crachás de controle interno)
--------------------------------------------------------------------------------
CREATE TABLE cartao (
    idCartao INT AUTO_INCREMENT PRIMARY KEY,                 -- Identificador único do cartão físico/código
    observacao TEXT NULL                                     -- Observações sobre o cartão (ex: Danificado, Reservado VIP)
);

--------------------------------------------------------------------------------
-- 6. TABELA DE COMANDA (Suporta identificação flexível: cartão, mesa, nome ou cliente)
--------------------------------------------------------------------------------
CREATE TABLE comanda (
    idComanda INT AUTO_INCREMENT PRIMARY KEY,                -- Identificador único interno da comanda
    numeroComanda VARCHAR(20) NULL,                          -- Número impresso da comanda de papel ou código customizado
    numeroMesa VARCHAR(10) NULL,                             -- Número ou identificação da mesa (ex: 'Mesa 05')
    nomeClienteProvisorio VARCHAR(100) NULL,                 -- Nome ou apelido livre caso o cliente não queira cadastro completo
    idCliente INT NULL,                                      -- ID do cliente cadastrado (Opcional: vinculado se desejar fidelidade/NF)
    dtAbertura DATETIME DEFAULT CURRENT_TIMESTAMP,           -- Data e hora de abertura da comanda
    dtFechamento DATETIME NULL,                              -- Data e hora de fechamento da conta
    valor DECIMAL(10,2) DEFAULT 0.00,                        -- Valor total bruto dos itens consumidos
    desconto DECIMAL(10,2) DEFAULT 0.00,                     -- Valor de desconto aplicado na comanda
    promocao VARCHAR(100) NULL,                              -- Nome ou código da promoção/cupom utilizado
    valorPago DECIMAL(10,2) DEFAULT 0.00,                    -- Valor efetivamente pago pelo cliente
    metodoPagamento VARCHAR(50) NULL,                        -- Forma de pagamento (ex: Dinheiro, Pix, Cartão de Crédito)
    status ENUM('Aberta', 'Fechada', 'Cancelada') DEFAULT 'Aberta', -- Situação atual da comanda
    CONSTRAINT fk_comanda_cliente FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)
);

--------------------------------------------------------------------------------
-- 7. TABELA ASSOCIATIVA CARTÃO X COMANDA (Vínculo N:N para cartões físicos)
--------------------------------------------------------------------------------
CREATE TABLE cartaoComanda (
    idComanda INT NOT NULL,                                  -- Referência à comanda aberta
    idCartao INT NOT NULL,                                   -- Referência ao cartão físico utilizado
    PRIMARY KEY (idComanda, idCartao),                       -- Chave primária composta para evitar duplicações
    CONSTRAINT fk_cc_comanda FOREIGN KEY (idComanda) REFERENCES comanda(idComanda),
    CONSTRAINT fk_cc_cartao FOREIGN KEY (idCartao) REFERENCES cartao(idCartao)
);

--------------------------------------------------------------------------------
-- 8. TABELA DE ITENS DA COMANDA (Produtos consumidos vinculados ao estoque)
--------------------------------------------------------------------------------
CREATE TABLE comandaItemEstoque (
    idComandaItem INT AUTO_INCREMENT PRIMARY KEY,            -- Identificador único do item lançado na comanda
    idComanda INT NOT NULL,                                  -- Referência à comanda correspondente
    idEstoque INT NOT NULL,                                  -- Referência ao lote do estoque que sofreu a baixa
    precovenda DECIMAL(10,2) NOT NULL,                       -- Preço unitário praticado no momento do pedido
    quantidade DECIMAL(10,3) NOT NULL,                       -- Quantidade consumida do item
    unidadeMedida VARCHAR(20) NOT NULL,                      -- Unidade de medida (ex: UN, KG, LT, PCT)
    dataPedido DATETIME DEFAULT CURRENT_TIMESTAMP,           -- Data e hora em que o item foi solicitado/pedido
    dataEntrega DATETIME NULL,                               -- Data e hora em que o item foi entregue ao cliente/mesa
    CONSTRAINT fk_cic_comanda FOREIGN KEY (idComanda) REFERENCES comanda(idComanda),
    CONSTRAINT fk_cic_estoque FOREIGN KEY (idEstoque) REFERENCES estoque(idEstoque)
);

--------------------------------------------------------------------------------
-- 9. TABELA DE ENCOMENDAS (Vendas futuras agendadas, ex: festas, salgados, bolos)
--------------------------------------------------------------------------------
CREATE TABLE encomenda (
    idEncomenda INT AUTO_INCREMENT PRIMARY KEY,              -- Identificador único da encomenda
    idCliente INT NOT NULL,                                  -- Referência obrigatória ao cliente que encomendou
    dtPedido DATETIME DEFAULT CURRENT_TIMESTAMP,             -- Data e hora em que a encomenda foi realizada
    dtEntrega DATETIME NULL,                                 -- Data e hora prevista para retirada ou entrega
    formaEntrega VARCHAR(50) NULL,                           -- Forma de entrega (ex: Retirada no Balcão, Delivery)
    formaPagto VARCHAR(50) NULL,                             -- Forma de pagamento acordada (ex: PIX, Cartão, Dinheiro)
    valorPagto DECIMAL(10,2) DEFAULT 0.00,                   -- Valor já pago (sinal ou pagamento antecipado)
    valorFrete DECIMAL(10,2) DEFAULT 0.00,                   -- Valor do frete/entrega (se houver)
    valorEncomenda DECIMAL(10,2) NOT NULL,                   -- Valor total da encomenda
    status VARCHAR(50) DEFAULT 'Pendente',                   -- Status atual (ex: Pendente, Em Produção, Concluída, Cancelada)
    CONSTRAINT fk_encomenda_cliente FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)
);

--------------------------------------------------------------------------------
-- 10. TABELA DE ITENS DA ENCOMENDA (Produtos vinculados à encomenda pré-agendada)
--------------------------------------------------------------------------------
CREATE TABLE encomendaItens (
    idEncomendaItem INT AUTO_INCREMENT PRIMARY KEY,          -- Identificador único do item da encomenda
    idEncomenda INT NOT NULL,                                -- Referência à encomenda principal
    idProduto INT NOT NULL,                                  -- Referência ao produto do catálogo base
    valorVenda DECIMAL(10,2) NOT NULL,                       -- Preço unitário cobrado no item da encomenda
    quantidade DECIMAL(10,3) NOT NULL,                       -- Quantidade solicitada do produto
    status VARCHAR(50) DEFAULT 'Pendente',                   -- Status individual do item (ex: Pendente, Produzindo, Pronto)
    dtEntrega DATETIME NULL,                                 -- Data e hora de entrega específica do item (se houver variação)
    CONSTRAINT fk_ei_encomenda FOREIGN KEY (idEncomenda) REFERENCES encomenda(idEncomenda),
    CONSTRAINT fk_ei_produto FOREIGN KEY (idProduto) REFERENCES produtos(idProduto)
);


--------------------------------------------------------------------------------
-- Listar encomendas pendentes de entrega para hoje
--------------------------------------------------------------------------------

SELECT 	e.idEncomenda, 
	c.nome AS cliente, 
	e.dtEntrega, 
	e.valorEncomenda, 
	e.status 
FROM 	encomenda e
JOIN 	cliente c ON e.idCliente = c.idCliente
WHERE	e.status != 'Pendente' 
AND 	DATE(e.dtEntrega) = CURDATE();
  
  
--------------------------------------------------------------------------------
-- Listar de clientes cadastrados
--------------------------------------------------------------------------------
  
SELECT
    idCliente,
    nome,
    email,
    cpf,
    telefone,
    cidade,
    uf,
    dtCadastro
FROM cliente
ORDER BY nome;

--------------------------------------------------------------------------------
-- Listar produtos cadastrados
--------------------------------------------------------------------------------

SELECT
    idProduto,
    nome,
    categoria,
    ingredientes
FROM produtos
ORDER BY nome;


--------------------------------------------------------------------------------
-- Listar estoque com nome do produto
--------------------------------------------------------------------------------

SELECT
    e.idEstoque,
    p.nome AS produto,
    p.categoria,
    e.quantidade,
    e.validade,
    e.lote,
    e.marca,
    e.fornecedor
FROM estoque e
INNER JOIN produtos p
    ON p.idProduto = e.idProduto
ORDER BY p.nome;

--------------------------------------------------------------------------------
-- Listar produtos com preço de compra e venda
--------------------------------------------------------------------------------

SELECT
    p.nome AS produto,
    e.idEstoque,
    e.quantidade,
    pi.valorCompra,
    pi.valorVenda,
    pi.dataPreco
FROM estoque e
INNER JOIN produtos p
    ON p.idProduto = e.idProduto
INNER JOIN precoItemEstoque pi
    ON pi.idEstoque = e.idEstoque
ORDER BY p.nome, pi.dataPreco DESC;

--------------------------------------------------------------------------------
-- Listar todas as comandas com dados dos clientes
--------------------------------------------------------------------------------

SELECT
    co.idComanda,
    co.numeroComanda,
    co.numeroMesa,
    COALESCE(c.nome, co.nomeClienteProvisorio) AS cliente,
    co.dtAbertura,
    co.dtFechamento,
    co.valor,
    co.desconto,
    co.valorPago,
    co.metodoPagamento,
    co.status
FROM comanda co
LEFT JOIN cliente c
    ON c.idCliente = co.idCliente
ORDER BY co.dtAbertura DESC;

--------------------------------------------------------------------------------
-- Listar comandas abertas
--------------------------------------------------------------------------------
  
  SELECT
    idComanda,
    numeroComanda,
    numeroMesa,
    nomeClienteProvisorio,
    dtAbertura,
    valor,
    desconto,
    valorPago
FROM comanda
WHERE status = 'Aberta'
ORDER BY dtAbertura;

--------------------------------------------------------------------------------
-- Listar itens consumidos em cada comanda
--------------------------------------------------------------------------------

SELECT
    ci.idComanda,
    co.numeroMesa,
    p.nome AS produto,
    ci.quantidade,
    ci.unidadeMedida,
    ci.precovenda,
    (ci.quantidade * ci.precovenda) AS subtotal,
    ci.dataPedido,
    ci.dataEntrega
FROM comandaItemEstoque ci
INNER JOIN comanda co
    ON co.idComanda = ci.idComanda
INNER JOIN estoque e
    ON e.idEstoque = ci.idEstoque
INNER JOIN produtos p
    ON p.idProduto = e.idProduto
ORDER BY ci.idComanda, ci.dataPedido;

--------------------------------------------------------------------------------
-- Listar encomendas com dados do cliente
--------------------------------------------------------------------------------

SELECT
    e.idEncomenda,
    c.nome AS cliente,
    c.telefone,
    e.dtPedido,
    e.dtEntrega,
    e.formaEntrega,
    e.formaPagto,
    e.valorEncomenda,
    e.valorPagto,
    (e.valorEncomenda - e.valorPagto) AS valorRestante,
    e.status
FROM encomenda e
INNER JOIN cliente c
    ON c.idCliente = e.idCliente
ORDER BY e.dtEntrega;

--------------------------------------------------------------------------------
-- Listar itens de cada encomenda
--------------------------------------------------------------------------------

SELECT
    ei.idEncomenda,
    p.nome AS produto,
    ei.quantidade,
    ei.valorVenda,
    (ei.quantidade * ei.valorVenda) AS subtotal,
    ei.status,
    ei.dtEntrega
FROM encomendaItens ei
INNER JOIN produtos p
    ON p.idProduto = ei.idProduto
WHERE ei.idEncomenda = 1
ORDER BY p.nome;

--------------------------------------------------------------------------------
-- Listar comandas pendentes do dia
--------------------------------------------------------------------------------

SELECT
    e.idEncomenda,
    c.nome AS cliente,
    c.telefone,
    e.dtEntrega,
    e.formaEntrega,
    e.valorEncomenda,
    e.valorPagto,
    e.status
FROM encomenda e
INNER JOIN cliente c
    ON c.idCliente = e.idCliente
WHERE e.status NOT IN ('Concluída', 'Cancelada')
  AND e.dtEntrega >= CURDATE()
  AND e.dtEntrega < CURDATE() + INTERVAL 1 DAY
ORDER BY e.dtEntrega;

--------------------------------------------------------------------------------
-- Agenda de encomenda dos próximos 7 dias
--------------------------------------------------------------------------------

SELECT
    e.idEncomenda,
    c.nome AS cliente,
    c.telefone,
    e.dtEntrega,
    e.formaEntrega,
    e.valorEncomenda,
    e.status
FROM encomenda e
INNER JOIN cliente c
    ON c.idCliente = e.idCliente
WHERE e.dtEntrega >= CURDATE()
  AND e.dtEntrega < CURDATE() + INTERVAL 7 DAY
  AND e.status NOT IN ('Concluída', 'Cancelada')
ORDER BY e.dtEntrega;

--------------------------------------------------------------------------------
-- Listar produtos estoque baixo ou zerado
--------------------------------------------------------------------------------

SELECT
    p.idProduto,
    p.nome AS produto,
    SUM(e.quantidade) AS quantidadeEstoque
FROM produtos p
LEFT JOIN estoque e
    ON e.idProduto = p.idProduto
GROUP BY p.idProduto, p.nome
HAVING COALESCE(SUM(e.quantidade), 0) < 5
ORDER BY quantidadeEstoque;

--------------------------------------------------------------------------------
-- Listar produtos próximo do vencimento
--------------------------------------------------------------------------------

SELECT
    p.nome AS produto,
    e.idEstoque,
    e.lote,
    e.quantidade,
    e.validade,
    DATEDIFF(e.validade, CURDATE()) AS diasParaVencer
FROM estoque e
INNER JOIN produtos p
    ON p.idProduto = e.idProduto
WHERE e.validade IS NOT NULL
  AND e.validade BETWEEN CURDATE()
                     AND CURDATE() + INTERVAL 30 DAY
  AND e.quantidade > 0
ORDER BY e.validade;

--------------------------------------------------------------------------------
-- Listar histórico entrada no estoque
--------------------------------------------------------------------------------

SELECT
    p.nome AS produto,
    e.quantidade,
    e.dtEntrada,
    e.fornecedor,
    e.notaEntrada,
    e.lote,
    e.validade
FROM estoque e
INNER JOIN produtos p
    ON p.idProduto = e.idProduto
ORDER BY e.dtEntrada DESC;

--------------------------------------------------------------------------------
-- Resumo comandas fechadas no dia
--------------------------------------------------------------------------------

SELECT
    COUNT(*) AS totalComandas,
    COALESCE(SUM(valor), 0) AS valorBruto,
    COALESCE(SUM(desconto), 0) AS totalDescontos,
    COALESCE(SUM(valorPago), 0) AS totalRecebido
FROM comanda
WHERE status = 'Fechada'
  AND dtFechamento >= CURDATE()
  AND dtFechamento < CURDATE() + INTERVAL 1 DAY;

--------------------------------------------------------------------------------
-- Total recebido por forma de pagamento
--------------------------------------------------------------------------------

SELECT
    metodoPagamento,
    COUNT(*) AS quantidadeComandas,
    SUM(valorPago) AS totalRecebido
FROM comanda
WHERE status = 'Fechada'
  AND dtFechamento >= CURDATE()
  AND dtFechamento < CURDATE() + INTERVAL 1 DAY
GROUP BY metodoPagamento
ORDER BY totalRecebido DESC;

--------------------------------------------------------------------------------
-- Listar comandas abertas
--------------------------------------------------------------------------------
