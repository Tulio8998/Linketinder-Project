export function configSkills() {
    const novaSkill = document.getElementById('input-skill') as HTMLInputElement
    const skillsClass = document.querySelector('.skills-class')

    novaSkill?.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault()

            const valorSkill = novaSkill.value.trim()

            if (valorSkill === '') {
                return
            }

            const skillDiv = document.createElement('div')
            skillDiv.className = 'skill-div'

            const skillName = document.createElement('p')
            skillName.className = 'skill'
            skillName.textContent = valorSkill

            const removeSkill = document.createElement('p')
            removeSkill.className = 'remove-skill'
            removeSkill.textContent = 'x'

            removeSkill.addEventListener('click', () => {
                skillDiv.remove()
            })

            skillDiv.appendChild(skillName)
            skillDiv.appendChild(removeSkill)

            skillsClass?.appendChild(skillDiv)

            novaSkill.value = ''
        }
    })
}