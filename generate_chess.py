import pygame
import re, io

f = open("Chess_Pieces_Sprite3.svg", "rb")
data = f.read()
f.close()

def replace(target: bytes, regex: bytes, replaceWith: bytes):
	while re.search(regex, target) != None:
		target = re.sub(regex, replaceWith, target)
	return target

data = replace(data, rb"<!--[ -~\t\n]*?-->", b"")
data = replace(data, rb"(?<=\n)[\n\t]+", b"")
dataBuffer = io.BytesIO(data)

f = open("chess_test2.svg", "wb")
f.write(data)
f.close()

img = pygame.image.load(dataBuffer, "Chess_Pieces_Sprite3.svg")

pygame.image.save(img, "src/resources/pieces.png")



"""

img = pygame.image.load("pieces.png")

# Flood fill
def flood_fill(img: pygame.Surface, pos: tuple[int, int]):
	img.set_at(pos, (0, 0, 0, 0))
	for x in [-1, 0, 1]:
		for y in [-1, 0, 1]:
			newPos = (pos[0] + x, pos[1] + y)
			if newPos[0] < 0 or newPos[1] < 0 or newPos[0] >= img.get_width() or newPos[1] >= img.get_height():
				continue
			if img.get_at(newPos) == (255, 255, 255, 255):
				flood_fill(img, newPos)
def flood_fill_flattened(img: pygame.Surface, startPos: tuple[int, int]):
	positions: list[tuple[int, int]] = [startPos]
	completed = 0
	while len(positions) > 0:
		pos = positions.pop(0)
		img.set_at(pos, (0, 0, 0, 0))
		completed += 1
		for x in [-1, 0, 1]:
			for y in [-1, 0, 1]:
				newPos = (pos[0] + x, pos[1] + y)
				if newPos[0] < 0 or newPos[1] < 0 or newPos[0] >= img.get_width() or newPos[1] >= img.get_height():
					continue
				if img.get_at(newPos) == (255, 255, 255, 255):
					positions.append(newPos)
		if len(positions) % 10 == 0: print(f"\r\u001b[2K{completed}/{len(positions) + completed} completed, {round(100 * completed / (len(positions)+completed), 1)}% done", end="")

flood_fill_flattened(img, (0, 0))

pygame.image.save(img, "src/resources/pieces.png")

"""
