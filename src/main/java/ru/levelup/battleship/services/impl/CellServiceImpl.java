package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.dao.CellsRepository;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.services.CellService;

@Service
@AllArgsConstructor
public class CellServiceImpl implements CellService {

    private CellsRepository repository;

    @Override
    @Transactional
    public Cell saveCell(Cell cell) {
        return repository.saveCell(cell);
    }

    @Override
    public Cell findCellByCoordinateXAndCoordinateY(int x, int y) {
        return repository.findCellByCoordinateXAndCoordinateY(x, y);
    }

    @Override
    public long countCellsByShip(Ship ship) {
        return repository.countCellsByShip(ship);
    }

    @Override
    @Transactional
    public void deleteCell(Cell cell) {
        repository.delete(cell);
    }
}