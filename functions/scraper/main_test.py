import main

import re


def test_get_formatted_card_name():
    assert 'xantcha-sleeper-agent' == main.get_formatted_card_name('Xantcha, Sleeper Agent')
    assert 'sol-ring' == main.get_formatted_card_name('Sol Ring')
    assert 'jeremys-amazing-card' == main.get_formatted_card_name("Jeremy's amazing c'ard")


def test_get_file_name():
    assert main.get_file_name(1).endswith('-page1')
    assert main.get_file_name(1).endswith('-page1')
    assert re.match(r'\d{8}-page5', main.get_file_name(5))


def test_get_full_filepath():
    filepath = main.get_full_filepath('Xantcha, Sleeper Agent', 3)
    print(filepath)
    assert filepath.startswith(main.dir)
    assert filepath.startswith(main.dir+'/xantcha-sleeper-agent')
    assert filepath.endswith('-page3')
    assert re.match(r''+main.dir+'/xantcha-sleeper-agent/\d{8}-page3', filepath)
