package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.dao.CellsRepository;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.CellService;

import java.util.List;

@Service
@AllArgsConstructor
public class CellServiceImpl implements CellService {

    private CellsRepository repository;

    @Override
    public long countCellsByShip(Ship ship) {
        return repository.countCellsByShip(ship);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCell(Cell cell) {
        repository.delete(cell);
    }

    @Override
    public Cell findCell(User user, int x, int y) {
        return repository.findCell(user, x, y);
    }

    @Override
    public List<Cell> findAll(User user) {
        return repository.findAll(user);
    }
}